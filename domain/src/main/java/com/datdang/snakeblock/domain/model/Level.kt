package com.datdang.snakeblock.domain.model

data class Level(
    val levelNumber: Int,
    val name: String,
    val difficulty: LevelDifficulty,
    val isUnlocked: Boolean,
    val bestMoves: Int?,
    val isCompleted: Boolean,
    val levelData: List<Triple<Int, Int, CellType>>
)

enum class LevelDifficulty {
    EASY, MEDIUM, HARD
}