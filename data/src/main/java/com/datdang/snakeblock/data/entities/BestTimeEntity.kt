package com.datdang.snakeblock.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "best_time")
data class BestTimeEntity(
    @PrimaryKey val gameName: String,
    val bestTimeMillis: Long,
    val score: Int? = null,
    val createdAt: Long = System.currentTimeMillis()
) 