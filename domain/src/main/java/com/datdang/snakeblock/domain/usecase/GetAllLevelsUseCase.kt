package com.datdang.snakeblock.domain.usecase

import com.datdang.snakeblock.domain.model.Level
import com.datdang.snakeblock.domain.repository.LevelRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllLevelsUseCase @Inject constructor(
    private val levelRepository: LevelRepository
) {
    operator fun invoke(): Flow<List<Level>> {
        return levelRepository.getAllLevels()
    }
}