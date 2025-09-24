package com.datdang.snakeblock.domain.usecase

import com.datdang.snakeblock.domain.game.SnakeBlockGameEngine
import com.datdang.snakeblock.domain.model.GameState

class GetGameStateUseCase(private val gameEngine: SnakeBlockGameEngine) {
    
    operator fun invoke(): GameState {
        return gameEngine.getCurrentGameState()
    }
}