package com.datdang.snakeblock.domain.repository

import com.datdang.snakeblock.domain.model.Level
import kotlinx.coroutines.flow.Flow

interface LevelRepository {
    fun getAllLevels(): Flow<List<Level>>
    suspend fun getLevel(levelNumber: Int): Level?
    fun getUnlockedLevels(): Flow<List<Level>>
    fun getCompletedLevels(): Flow<List<Level>>
    suspend fun updateLevelCompletion(levelNumber: Int, moves: Int)
    suspend fun unlockLevel(levelNumber: Int)
    suspend fun initializeLevelsIfNeeded()
}