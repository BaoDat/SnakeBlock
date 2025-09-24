package com.datdang.snakeblock.data.db.game

import androidx.room.Database
import androidx.room.RoomDatabase
import com.datdang.data.db.BestTimeDao
import com.datdang.snakeblock.data.entities.BestTimeEntity

@Database(
    entities = [BestTimeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GameDatabase : RoomDatabase() {
    abstract fun bestTimeDao(): BestTimeDao
}