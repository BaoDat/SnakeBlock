package com.datdang.snakeblock.domain.usecase

import com.datdang.snakeblock.domain.repository.SnakeBlockRepository

class SaveBestData(private val repo: SnakeBlockRepository) {
    suspend fun saveBestTime(gameName: String, time: Long) = repo.saveBestTime(gameName, time)
    suspend fun saveBestTimeAndScore(gameName: String, time: Long, score: Int) = repo.saveBestTimeAndScore(gameName, time, score)
}