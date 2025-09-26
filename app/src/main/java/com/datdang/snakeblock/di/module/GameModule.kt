package com.datdang.snakeblock.di.module

import com.datdang.snakeblock.domain.game.SnakeBlockGameEngine
import com.datdang.snakeblock.domain.usecase.*
import com.datdang.snakeblock.domain.repository.LevelRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object GameModule {

    @Provides
    @ViewModelScoped
    fun provideSnakeBlockGameEngine(): SnakeBlockGameEngine {
        return SnakeBlockGameEngine()
    }

    @Provides
    @ViewModelScoped
    fun provideSetupLevelUseCase(
        gameEngine: SnakeBlockGameEngine,
        levelRepository: LevelRepository
    ): SetupLevelUseCase {
        return SetupLevelUseCase(gameEngine, levelRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideMoveSnakeUseCase(gameEngine: SnakeBlockGameEngine): MoveSnakeUseCase {
        return MoveSnakeUseCase(gameEngine)
    }

    @Provides
    @ViewModelScoped
    fun provideUndoMoveUseCase(gameEngine: SnakeBlockGameEngine): UndoMoveUseCase {
        return UndoMoveUseCase(gameEngine)
    }

    @Provides
    @ViewModelScoped
    fun provideGetGameStateUseCase(gameEngine: SnakeBlockGameEngine): GetGameStateUseCase {
        return GetGameStateUseCase(gameEngine)
    }

    @Provides
    @ViewModelScoped
    fun provideGetLevelUseCase(levelRepository: LevelRepository): GetLevelUseCase {
        return GetLevelUseCase(levelRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllLevelsUseCase(levelRepository: LevelRepository): GetAllLevelsUseCase {
        return GetAllLevelsUseCase(levelRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideCompleteLevelUseCase(levelRepository: LevelRepository): CompleteLevelUseCase {
        return CompleteLevelUseCase(levelRepository)
    }
}