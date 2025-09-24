package com.datdang.snakeblock.domain.model

sealed class MoveResult {
    object Success : MoveResult()
    data class Error(val message: String) : MoveResult()
}