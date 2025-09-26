package com.datdang.snakeblock.di.module

import android.content.Context
import com.datdang.snakeblock.data.db.game.GameDatabase
import com.datdang.snakeblock.data.dao.LevelDao
import com.datdang.snakeblock.data.repository.LevelRepositoryImpl
import com.datdang.snakeblock.domain.repository.LevelRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGameDatabase(@ApplicationContext context: Context): GameDatabase {
        return GameDatabase.getDatabase(context)
    }

    @Provides
    fun provideLevelDao(database: GameDatabase): LevelDao {
        return database.levelDao()
    }

    @Provides
    @Singleton
    fun provideLevelRepository(levelDao: LevelDao): LevelRepository {
        return LevelRepositoryImpl(levelDao)
    }
}
