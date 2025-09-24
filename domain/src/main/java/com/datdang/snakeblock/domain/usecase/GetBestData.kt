package com.datdang.snakeblock.domain.usecase

import com.datdang.snakeblock.domain.repository.SnakeBlockRepository

class GetBestData(private val repo: SnakeBlockRepository) {
    suspend fun getBestTime(gameName: String): Long = repo.getBestTime(gameName)
    suspend fun getBestScore(gameName: String): Int = repo.getBestScore(gameName)
} 