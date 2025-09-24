package com.datdang.snakeblock.domain.usecase

import com.datdang.snakeblock.domain.game.SnakeBlockGameEngine
import com.datdang.snakeblock.domain.model.CellType

class SetupLevelUseCase(private val gameEngine: SnakeBlockGameEngine) {
    
    operator fun invoke(levelData: List<Triple<Int, Int, CellType>>) {
        gameEngine.setupLevel(levelData)
    }
    
    // Example level data
    fun getLevel1Data(): List<Triple<Int, Int, CellType>> {
        return listOf(
            // Create 2x2 square with red blocks
            Triple(2, 2, CellType.RED_BLOCK),
            Triple(2, 3, CellType.RED_BLOCK),
            Triple(3, 2, CellType.RED_BLOCK),
            Triple(3, 3, CellType.RED_BLOCK),
            // Snake Head starts at top-left corner of square
            Triple(2, 2, CellType.SNAKE_HEAD)
        )
    }
    
    fun getLevel2Data(): List<Triple<Int, Int, CellType>> {
        return listOf(
            // Create L-shape with red blocks
            Triple(1, 1, CellType.RED_BLOCK),
            Triple(1, 2, CellType.RED_BLOCK),
            Triple(1, 3, CellType.RED_BLOCK),
            Triple(2, 1, CellType.RED_BLOCK),
            Triple(3, 1, CellType.RED_BLOCK),
            // Snake Head starts at bottom-left corner
            Triple(3, 1, CellType.SNAKE_HEAD)
        )
    }
    
    fun getLevel3Data(): List<Triple<Int, Int, CellType>> {
        return listOf(
            // Create long straight line
            Triple(4, 2, CellType.RED_BLOCK),
            Triple(4, 3, CellType.RED_BLOCK),
            Triple(4, 4, CellType.RED_BLOCK),
            Triple(4, 5, CellType.RED_BLOCK),
            Triple(4, 6, CellType.RED_BLOCK),
            // Snake Head starts at left end
            Triple(4, 2, CellType.SNAKE_HEAD)
        )
    }
}