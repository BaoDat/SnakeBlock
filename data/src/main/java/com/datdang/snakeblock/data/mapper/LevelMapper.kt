package com.datdang.snakeblock.data.mapper

import com.datdang.snakeblock.data.entities.LevelEntity
import com.datdang.snakeblock.data.entities.LevelCellEntity
import com.datdang.snakeblock.domain.model.Level
import com.datdang.snakeblock.domain.model.LevelDifficulty
import com.datdang.snakeblock.domain.model.CellType

fun LevelEntity.toDomain(cells: List<LevelCellEntity>): Level {
    val levelData = cells.map { cell ->
        Triple(
            cell.row,
            cell.col,
            when (cell.cellType) {
                "RED_BLOCK" -> CellType.RED_BLOCK
                "SNAKE_HEAD" -> CellType.SNAKE_HEAD
                else -> CellType.EMPTY
            }
        )
    }
    
    return Level(
        levelNumber = levelNumber,
        name = name,
        difficulty = when (difficulty) {
            "EASY" -> LevelDifficulty.EASY
            "MEDIUM" -> LevelDifficulty.MEDIUM
            "HARD" -> LevelDifficulty.HARD
            else -> LevelDifficulty.EASY
        },
        isUnlocked = isUnlocked,
        bestMoves = bestMoves,
        isCompleted = isCompleted,
        levelData = levelData
    )
}

fun Level.toEntity(): LevelEntity {
    return LevelEntity(
        levelNumber = levelNumber,
        name = name,
        difficulty = difficulty.name,
        isUnlocked = isUnlocked,
        bestMoves = bestMoves,
        isCompleted = isCompleted
    )
}

fun Level.toCellEntities(): List<LevelCellEntity> {
    return levelData.map { (row, col, cellType) ->
        LevelCellEntity(
            levelNumber = levelNumber,
            row = row,
            col = col,
            cellType = when (cellType) {
                CellType.RED_BLOCK -> "RED_BLOCK"
                CellType.SNAKE_HEAD -> "SNAKE_HEAD"
                else -> "EMPTY"
            }
        )
    }
}