package com.datdang.snakeblock.data.repository

import com.datdang.snakeblock.data.dao.LevelDao
import com.datdang.snakeblock.data.entities.LevelEntity
import com.datdang.snakeblock.data.entities.LevelCellEntity
import com.datdang.snakeblock.data.mapper.toDomain
import com.datdang.snakeblock.domain.model.Level
import com.datdang.snakeblock.domain.repository.LevelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LevelRepositoryImpl @Inject constructor(
    private val levelDao: LevelDao
) : LevelRepository {

    override fun getAllLevels(): Flow<List<Level>> {
        return levelDao.getAllLevels().map { entities ->
            entities.map { entity ->
                val cells = levelDao.getLevelCells(entity.levelNumber)
                entity.toDomain(cells)
            }
        }
    }

    override suspend fun getLevel(levelNumber: Int): Level? {
        val entity = levelDao.getLevel(levelNumber) ?: return null
        val cells = levelDao.getLevelCells(levelNumber)
        return entity.toDomain(cells)
    }

    override fun getUnlockedLevels(): Flow<List<Level>> {
        return levelDao.getUnlockedLevels().map { entities ->
            entities.map { entity ->
                val cells = levelDao.getLevelCells(entity.levelNumber)
                entity.toDomain(cells)
            }
        }
    }

    override fun getCompletedLevels(): Flow<List<Level>> {
        return levelDao.getCompletedLevels().map { entities ->
            entities.map { entity ->
                val cells = levelDao.getLevelCells(entity.levelNumber)
                entity.toDomain(cells)
            }
        }
    }

    override suspend fun updateLevelCompletion(levelNumber: Int, moves: Int) {
        levelDao.updateLevelCompletion(levelNumber, moves)
        // Unlock next level
        if (levelNumber < 100) {
            levelDao.unlockLevel(levelNumber + 1)
        }
    }

    override suspend fun unlockLevel(levelNumber: Int) {
        levelDao.unlockLevel(levelNumber)
    }

    override suspend fun initializeLevelsIfNeeded() {
        // Check if levels are already initialized
        val existingLevel = levelDao.getLevel(1)
        if (existingLevel == null) {
            // Initialize all 100 levels
            initializeAllLevels()
        }
    }

    private suspend fun initializeAllLevels() {
        // This will be called when database is first created
        // The levels will be pre-populated from the asset database file
        // So we just need to unlock the first level
        levelDao.unlockLevel(1)
    }
}