package com.datdang.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.datdang.snakeblock.data.entities.BestTimeEntity

@Dao
interface BestTimeDao {
    @Query("SELECT * FROM best_time WHERE gameName = :gameName LIMIT 1")
    suspend fun getBestData(gameName: String): BestTimeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateBestTime(entity: BestTimeEntity)
} 