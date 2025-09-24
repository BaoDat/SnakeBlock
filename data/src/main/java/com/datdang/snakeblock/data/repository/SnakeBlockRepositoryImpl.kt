package com.datdang.snakeblock.data.repository

import com.datdang.data.db.BestTimeDao
import com.datdang.snakeblock.data.entities.BestTimeEntity
import com.datdang.snakeblock.domain.repository.SnakeBlockRepository

class SnakeBlockRepositoryImpl(private val dao: BestTimeDao) : SnakeBlockRepository {
    override suspend fun getBestTime(gameName: String): Long {
        return dao.getBestData(gameName)?.bestTimeMillis ?: 0L
    }

    override suspend fun getBestScore(gameName: String): Int {
        return dao.getBestData(gameName)?.score ?: 0
    }

    override suspend fun saveBestTime(gameName: String, time: Long) {
        dao.insertOrUpdateBestTime(BestTimeEntity(gameName, time))
    }
    override suspend fun saveBestTimeAndScore(gameName: String, time: Long, score: Int) {
        dao.insertOrUpdateBestTime(BestTimeEntity(gameName, time, score))
    }
}