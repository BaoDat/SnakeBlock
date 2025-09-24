package com.datdang.snakeblock.domain.usecase

import com.datdang.snakeblock.domain.game.SnakeBlockGameEngine

class UndoMoveUseCase(private val gameEngine: SnakeBlockGameEngine) {
    
    operator fun invoke(): Boolean {
        return gameEngine.undoMove()
    }
    
    fun canUndo(): Boolean {
        return gameEngine.canUndo()
    }
}