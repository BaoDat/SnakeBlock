package com.datdang.snakeblock.domain.usecase

import com.datdang.snakeblock.domain.game.SnakeBlockGameEngine
import com.datdang.snakeblock.domain.model.Coordinate
import com.datdang.snakeblock.domain.model.MoveResult

class MoveSnakeUseCase(private val gameEngine: SnakeBlockGameEngine) {
    
    operator fun invoke(targetCoordinate: Coordinate): MoveResult {
        return gameEngine.moveSnake(targetCoordinate)
    }
}