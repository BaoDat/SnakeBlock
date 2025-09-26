package com.datdang.snakeblock.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levels")
data class LevelEntity(
    @PrimaryKey
    val levelNumber: Int,
    val name: String,
    val difficulty: String = "EASY", // EASY, MEDIUM, HARD
    val isUnlocked: Boolean = false,
    val bestMoves: Int? = null,
    val isCompleted: Boolean = false
)