package com.datdang.snakeblock.domain.usecase

import com.datdang.snakeblock.domain.model.Level
import com.datdang.snakeblock.domain.repository.LevelRepository
import javax.inject.Inject

class GetLevelUseCase @Inject constructor(
    private val levelRepository: LevelRepository
) {
    suspend operator fun invoke(levelNumber: Int): Level? {
        return levelRepository.getLevel(levelNumber)
    }
}