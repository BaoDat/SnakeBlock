package com.datdang.snakeblock.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datdang.snakeblock.domain.game.SnakeBlockGameEngine
import com.datdang.snakeblock.domain.model.*
import com.datdang.snakeblock.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameEngine: SnakeBlockGameEngine,
    private val setupLevelUseCase: SetupLevelUseCase,
    private val moveSnakeUseCase: MoveSnakeUseCase,
    private val undoMoveUseCase: UndoMoveUseCase,
    private val getGameStateUseCase: GetGameStateUseCase,
    private val getLevelUseCase: GetLevelUseCase,
    private val completeLevelUseCase: CompleteLevelUseCase
) : ViewModel() {

    private val _gameState = MutableStateFlow(
        GameState(
            board = Array(SnakeBlockGameEngine.GRID_SIZE) { Array(SnakeBlockGameEngine.GRID_SIZE) { CellType.EMPTY } },
            snakePath = emptyList(),
            totalRedBlocks = 0,
            redBlocksFilled = 0
        )
    )
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _canUndo = MutableStateFlow(false)
    val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

    private val _isGameWon = MutableStateFlow(false)
    val isGameWon: StateFlow<Boolean> = _isGameWon.asStateFlow()

    private val _currentLevel = MutableStateFlow(1)
    val currentLevel: StateFlow<Int> = _currentLevel.asStateFlow()

    private val _moveCount = MutableStateFlow(0)
    val moveCount: StateFlow<Int> = _moveCount.asStateFlow()

    fun startLevel(levelNumber: Int) {
        viewModelScope.launch {
            try {
                _currentLevel.value = levelNumber
                _moveCount.value = 0
                _isGameWon.value = false
                setupLevelUseCase(levelNumber)
                updateGameState()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load level $levelNumber: ${e.message}"
            }
        }
    }

        private suspend fun completeCurrentLevel() {
        try {
            completeLevelUseCase(_currentLevel.value, _moveCount.value)
        } catch (e: Exception) {
            // Log error but don't show to user as level is already completed
        }
    }

    fun moveSnake(targetCoordinate: Coordinate) {
        viewModelScope.launch {
            when (val result = moveSnakeUseCase(targetCoordinate)) {
                is MoveResult.Success -> {
                    _moveCount.value += 1
                    updateGameState()
                    _errorMessage.value = null
                    
                    // Check win condition
                    if (gameEngine.checkWin()) {
                        _isGameWon.value = true
                        completeCurrentLevel()
                    }
                }
                is MoveResult.Error -> {
                    _errorMessage.value = result.message
                }
            }
        }
    }

    fun undoMove() {
        viewModelScope.launch {
            if (undoMoveUseCase()) {
                if (_moveCount.value > 0) {
                    _moveCount.value -= 1
                }
                _isGameWon.value = false
                updateGameState()
                _errorMessage.value = null
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun resetGame() {
        _moveCount.value = 0
        _isGameWon.value = false
//        gameEngine.resetGame()
        updateGameState()
    }

    fun nextLevel() {
        val nextLevelNumber = _currentLevel.value + 1
        startLevel(nextLevelNumber)
    }

    private fun updateGameState() {
        _gameState.value = getGameStateUseCase()
        _canUndo.value = undoMoveUseCase.canUndo()
    }

    // Helper function to convert screen coordinates to grid coordinates
    fun screenToGridCoordinate(x: Float, y: Float, cellSize: Float): Coordinate? {
        val col = (x / cellSize).toInt()
        val row = (y / cellSize).toInt()
        
        return if (row in 0 until SnakeBlockGameEngine.GRID_SIZE && 
                   col in 0 until SnakeBlockGameEngine.GRID_SIZE) {
            Coordinate(row, col)
        } else {
            null
        }
    }
}