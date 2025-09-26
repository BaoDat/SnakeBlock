package com.datdang.snakeblock.data.db.game

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.datdang.data.db.BestTimeDao
import com.datdang.snakeblock.data.entities.BestTimeEntity
import com.datdang.snakeblock.data.entities.LevelEntity
import com.datdang.snakeblock.data.entities.LevelCellEntity
import com.datdang.snakeblock.data.dao.LevelDao

@Database(
    entities = [BestTimeEntity::class, LevelEntity::class, LevelCellEntity::class],
    version = 2,
    exportSchema = false
)
abstract class GameDatabase : RoomDatabase() {
    abstract fun bestTimeDao(): BestTimeDao
    abstract fun levelDao(): LevelDao
    
    companion object {
        const val DATABASE_NAME = "snake_block_game.db"
        
        @Volatile
        private var INSTANCE: GameDatabase? = null
        
        fun getDatabase(context: Context): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    DATABASE_NAME
                )
                .createFromAsset("database/snake_block_levels.db")
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}