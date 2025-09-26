package com.datdang.snakeblock.domain.usecase

import com.datdang.snakeblock.domain.repository.LevelRepository
import javax.inject.Inject

class CompleteLevelUseCase @Inject constructor(
    private val levelRepository: LevelRepository
) {
    suspend operator fun invoke(levelNumber: Int, moves: Int) {
        levelRepository.updateLevelCompletion(levelNumber, moves)
    }
}