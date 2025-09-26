package scripts

import com.datdang.snakeblock.domain.model.CellType
import java.io.File
import kotlin.random.Random

/**
 * Script để tạo database SQLite với 100 level
 * Chạy script này để tạo file snake_block_levels.db
 */
fun main() {
    val dbFile = File("app/src/main/assets/database/snake_block_levels.db")
    dbFile.parentFile.mkdirs()
    
    val sqlStatements = mutableListOf<String>()
    
    // Create tables
    sqlStatements.add("""
        CREATE TABLE IF NOT EXISTS levels (
            levelNumber INTEGER PRIMARY KEY,
            name TEXT NOT NULL,
            difficulty TEXT NOT NULL DEFAULT 'EASY',
            isUnlocked INTEGER NOT NULL DEFAULT 0,
            bestMoves INTEGER,
            isCompleted INTEGER NOT NULL DEFAULT 0
        );
    """.trimIndent())
    
    sqlStatements.add("""
        CREATE TABLE IF NOT EXISTS level_cells (
            levelNumber INTEGER,
            row INTEGER,
            col INTEGER,
            cellType TEXT NOT NULL,
            PRIMARY KEY (levelNumber, row, col),
            FOREIGN KEY (levelNumber) REFERENCES levels(levelNumber) ON DELETE CASCADE
        );
    """.trimIndent())
    
    // Generate 100 levels
    for (i in 1..100) {
        val level = generateLevel(i)
        
        // Insert level
        val difficulty = when {
            i <= 30 -> "EASY"
            i <= 70 -> "MEDIUM"
            else -> "HARD"
        }
        
        val isUnlocked = if (i == 1) 1 else 0
        
        sqlStatements.add("""
            INSERT INTO levels (levelNumber, name, difficulty, isUnlocked, isCompleted) 
            VALUES ($i, 'Level $i', '$difficulty', $isUnlocked, 0);
        """.trimIndent())
        
        // Insert level cells
        level.forEach { (row, col, cellType) ->
            val cellTypeStr = when (cellType) {
                CellType.RED_BLOCK -> "RED_BLOCK"
                CellType.SNAKE_HEAD -> "SNAKE_HEAD"
                else -> "EMPTY"
            }
            sqlStatements.add("""
                INSERT INTO level_cells (levelNumber, row, col, cellType) 
                VALUES ($i, $row, $col, '$cellTypeStr');
            """.trimIndent())
        }
    }
    
    // Write SQL file
    val sqlFile = File("generate_levels.sql")
    sqlFile.writeText(sqlStatements.joinToString("\n\n"))
    
    println("Generated SQL file: ${sqlFile.absolutePath}")
    println("Run this SQL file in SQLite to create the database:")
    println("sqlite3 ${dbFile.absolutePath} < ${sqlFile.absolutePath}")
}

fun generateLevel(levelNumber: Int): List<Triple<Int, Int, CellType>> {
    val random = Random(levelNumber) // Seed để consistent
    val cells = mutableListOf<Triple<Int, Int, CellType>>()
    
    when {
        levelNumber <= 10 -> generateSimpleLevel(levelNumber, cells, random)
        levelNumber <= 30 -> generateEasyLevel(levelNumber, cells, random)
        levelNumber <= 70 -> generateMediumLevel(levelNumber, cells, random)
        else -> generateHardLevel(levelNumber, cells, random)
    }
    
    return cells
}

fun generateSimpleLevel(levelNumber: Int, cells: MutableList<Triple<Int, Int, CellType>>, random: Random) {
    when (levelNumber) {
        1 -> {
            // 2x2 square
            cells.add(Triple(2, 2, CellType.RED_BLOCK))
            cells.add(Triple(2, 3, CellType.RED_BLOCK))
            cells.add(Triple(3, 2, CellType.RED_BLOCK))
            cells.add(Triple(3, 3, CellType.RED_BLOCK))
            cells.add(Triple(2, 2, CellType.SNAKE_HEAD))
        }
        2 -> {
            // L-shape
            cells.add(Triple(1, 1, CellType.RED_BLOCK))
            cells.add(Triple(1, 2, CellType.RED_BLOCK))
            cells.add(Triple(1, 3, CellType.RED_BLOCK))
            cells.add(Triple(2, 1, CellType.RED_BLOCK))
            cells.add(Triple(3, 1, CellType.RED_BLOCK))
            cells.add(Triple(3, 1, CellType.SNAKE_HEAD))
        }
        3 -> {
            // Straight line
            for (col in 2..6) {
                cells.add(Triple(4, col, CellType.RED_BLOCK))
            }
            cells.add(Triple(4, 2, CellType.SNAKE_HEAD))
        }
        else -> {
            // Random simple patterns
            val size = 3 + (levelNumber % 3)
            val startRow = random.nextInt(2, 8 - size)
            val startCol = random.nextInt(2, 8 - size)
            
            for (r in startRow until startRow + size) {
                for (c in startCol until startCol + size) {
                    if (random.nextFloat() < 0.7f) {
                        cells.add(Triple(r, c, CellType.RED_BLOCK))
                    }
                }
            }
            
            if (cells.isNotEmpty()) {
                val firstBlock = cells.first()
                cells.add(Triple(firstBlock.first, firstBlock.second, CellType.SNAKE_HEAD))
            }
        }
    }
}

fun generateEasyLevel(levelNumber: Int, cells: MutableList<Triple<Int, Int, CellType>>, random: Random) {
    val patterns = listOf("rectangle", "cross", "diagonal", "spiral")
    val pattern = patterns[levelNumber % patterns.size]
    
    when (pattern) {
        "rectangle" -> {
            val width = 3 + random.nextInt(3)
            val height = 2 + random.nextInt(3)
            val startRow = random.nextInt(1, 9 - height)
            val startCol = random.nextInt(1, 9 - width)
            
            for (r in startRow until startRow + height) {
                for (c in startCol until startCol + width) {
                    cells.add(Triple(r, c, CellType.RED_BLOCK))
                }
            }
            cells.add(Triple(startRow, startCol, CellType.SNAKE_HEAD))
        }
        "cross" -> {
            val centerRow = 4
            val centerCol = 4
            val size = 2
            
            // Horizontal line
            for (c in centerCol - size..centerCol + size) {
                cells.add(Triple(centerRow, c, CellType.RED_BLOCK))
            }
            // Vertical line
            for (r in centerRow - size..centerRow + size) {
                cells.add(Triple(r, centerCol, CellType.RED_BLOCK))
            }
            cells.add(Triple(centerRow, centerCol, CellType.SNAKE_HEAD))
        }
        else -> generateSimpleLevel(levelNumber, cells, random)
    }
}

fun generateMediumLevel(levelNumber: Int, cells: MutableList<Triple<Int, Int, CellType>>, random: Random) {
    // More complex patterns
    val blockCount = 8 + random.nextInt(8)
    val usedPositions = mutableSetOf<Pair<Int, Int>>()
    
    repeat(blockCount) {
        var row: Int
        var col: Int
        do {
            row = random.nextInt(1, 8)
            col = random.nextInt(1, 8)
        } while (usedPositions.contains(row to col))
        
        usedPositions.add(row to col)
        cells.add(Triple(row, col, CellType.RED_BLOCK))
    }
    
    if (cells.isNotEmpty()) {
        val startBlock = cells[random.nextInt(cells.size)]
        cells.add(Triple(startBlock.first, startBlock.second, CellType.SNAKE_HEAD))
    }
}

fun generateHardLevel(levelNumber: Int, cells: MutableList<Triple<Int, Int, CellType>>, random: Random) {
    // Very complex patterns
    val blockCount = 12 + random.nextInt(10)
    val usedPositions = mutableSetOf<Pair<Int, Int>>()
    
    // Create clusters
    val clusterCount = 2 + random.nextInt(3)
    repeat(clusterCount) {
        val centerRow = random.nextInt(2, 7)
        val centerCol = random.nextInt(2, 7)
        val clusterSize = 3 + random.nextInt(4)
        
        repeat(clusterSize) {
            val row = centerRow + random.nextInt(-2, 3)
            val col = centerCol + random.nextInt(-2, 3)
            
            if (row in 0..8 && col in 0..8 && !usedPositions.contains(row to col)) {
                usedPositions.add(row to col)
                cells.add(Triple(row, col, CellType.RED_BLOCK))
            }
        }
    }
    
    if (cells.isNotEmpty()) {
        val startBlock = cells[random.nextInt(cells.size)]
        cells.add(Triple(startBlock.first, startBlock.second, CellType.SNAKE_HEAD))
    }
}