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
    private val getGameStateUseCase: GetGameStateUseCase
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

    fun startLevel(levelNumber: Int) {
        viewModelScope.launch {
            val levelData = when (levelNumber) {
                1 -> setupLevelUseCase.getLevel1Data()
                2 -> setupLevelUseCase.getLevel2Data()
                3 -> setupLevelUseCase.getLevel3Data()
                else -> setupLevelUseCase.getLevel1Data() // Default to level 1
            }
            
            setupLevelUseCase(levelData)
            updateGameState()
        }
    }

    fun moveSnake(targetCoordinate: Coordinate) {
        viewModelScope.launch {
            when (val result = moveSnakeUseCase(targetCoordinate)) {
                is MoveResult.Success -> {
                    updateGameState()
                    _errorMessage.value = null
                    
                    // Check win condition
                    if (gameEngine.checkWin()) {
                        _isGameWon.value = true
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
                updateGameState()
                _errorMessage.value = null
                _isGameWon.value = false // Reset win state when undoing
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun resetGame() {
        _isGameWon.value = false
        startLevel(1) // Reset to level 1
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