package com.datdang.snakeblock.di.module

import android.content.Context
import com.datdang.data.db.BestTimeDao
import com.datdang.snakeblock.domain.usecase.SaveBestData
import com.datdang.snakeblock.domain.usecase.GetBestData
import com.datdang.snakeblock.data.db.game.GameDatabase
import com.datdang.snakeblock.data.repository.SnakeBlockRepositoryImpl
import com.datdang.snakeblock.data.util.NetworkMonitorImpl
import com.datdang.snakeblock.domain.repository.SnakeBlockRepository
import com.datdang.snakeblock.domain.util.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideBestTimeDao(db: GameDatabase): BestTimeDao = db.bestTimeDao()

    @Provides
    @Singleton
    fun provideGameRepository(bestTimeDao: BestTimeDao): SnakeBlockRepository = SnakeBlockRepositoryImpl(bestTimeDao)

    @Provides
    fun provideGetBestTimeUseCase(repo: SnakeBlockRepository): GetBestData = GetBestData(repo)

    @Provides
    fun provideSaveDataUseCase(repo: SnakeBlockRepository): SaveBestData = SaveBestData(repo)

    @Provides
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor = NetworkMonitorImpl(context)
}