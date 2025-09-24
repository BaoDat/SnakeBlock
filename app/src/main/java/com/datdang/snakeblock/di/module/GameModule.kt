package com.datdang.snakeblock.di.module

import com.datdang.snakeblock.domain.game.SnakeBlockGameEngine
import com.datdang.snakeblock.domain.usecase.*
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
    fun provideSetupLevelUseCase(gameEngine: SnakeBlockGameEngine): SetupLevelUseCase {
        return SetupLevelUseCase(gameEngine)
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
}