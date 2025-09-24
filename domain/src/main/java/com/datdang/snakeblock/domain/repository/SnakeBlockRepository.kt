package com.datdang.snakeblock.domain.repository

interface SnakeBlockRepository {
    suspend fun getBestTime(gameName: String): Long
    suspend fun getBestScore(gameName: String): Int
    suspend fun saveBestTime(gameName: String, time: Long)
    suspend fun saveBestTimeAndScore(gameName: String, time: Long, score: Int)
}