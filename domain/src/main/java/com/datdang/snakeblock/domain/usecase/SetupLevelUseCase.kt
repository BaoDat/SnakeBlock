package com.datdang.snakeblock.domain.usecase

import com.datdang.snakeblock.domain.game.SnakeBlockGameEngine
import com.datdang.snakeblock.domain.model.CellType
import com.datdang.snakeblock.domain.repository.LevelRepository

class SetupLevelUseCase(
    private val gameEngine: SnakeBlockGameEngine,
    private val levelRepository: LevelRepository
) {
    
    suspend operator fun invoke(levelNumber: Int) {
        // Initialize levels if needed (first time app runs)
        levelRepository.initializeLevelsIfNeeded()
        
        val level = levelRepository.getLevel(levelNumber)
        if (level != null) {
            gameEngine.setupLevel(level.levelData)
        } else {
            // Fallback to default level if not found
            gameEngine.setupLevel(getDefaultLevelData())
        }
    }
    
    // Legacy method for direct level data setup (backward compatibility)
    operator fun invoke(levelData: List<Triple<Int, Int, CellType>>) {
        gameEngine.setupLevel(levelData)
    }
    
    private fun getDefaultLevelData(): List<Triple<Int, Int, CellType>> {
        return listOf(
            Triple(2, 2, CellType.RED_BLOCK),
            Triple(2, 3, CellType.RED_BLOCK),
            Triple(3, 2, CellType.RED_BLOCK),
            Triple(3, 3, CellType.RED_BLOCK),
            Triple(2, 2, CellType.SNAKE_HEAD)
        )
    }
}