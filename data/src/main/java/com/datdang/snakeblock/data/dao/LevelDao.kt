package com.datdang.snakeblock.data.dao

import androidx.room.*
import com.datdang.snakeblock.data.entities.LevelEntity
import com.datdang.snakeblock.data.entities.LevelCellEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LevelDao {
    
    @Query("SELECT * FROM levels ORDER BY levelNumber ASC")
    fun getAllLevels(): Flow<List<LevelEntity>>
    
    @Query("SELECT * FROM levels WHERE levelNumber = :levelNumber")
    suspend fun getLevel(levelNumber: Int): LevelEntity?
    
    @Query("SELECT * FROM level_cells WHERE levelNumber = :levelNumber")
    suspend fun getLevelCells(levelNumber: Int): List<LevelCellEntity>
    
    @Query("SELECT * FROM levels WHERE isUnlocked = 1 ORDER BY levelNumber ASC")
    fun getUnlockedLevels(): Flow<List<LevelEntity>>
    
    @Query("SELECT * FROM levels WHERE isCompleted = 1 ORDER BY levelNumber ASC")
    fun getCompletedLevels(): Flow<List<LevelEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevel(level: LevelEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevelCells(cells: List<LevelCellEntity>)
    
    @Update
    suspend fun updateLevel(level: LevelEntity)
    
    @Query("UPDATE levels SET isCompleted = 1, bestMoves = :moves WHERE levelNumber = :levelNumber AND (bestMoves IS NULL OR bestMoves > :moves)")
    suspend fun updateLevelCompletion(levelNumber: Int, moves: Int)
    
    @Query("UPDATE levels SET isUnlocked = 1 WHERE levelNumber = :levelNumber")
    suspend fun unlockLevel(levelNumber: Int)
    
    @Transaction
    suspend fun insertLevelWithCells(level: LevelEntity, cells: List<LevelCellEntity>) {
        insertLevel(level)
        insertLevelCells(cells)
    }
}