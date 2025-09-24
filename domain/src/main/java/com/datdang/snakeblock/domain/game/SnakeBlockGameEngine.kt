package com.datdang.snakeblock.domain.game

import com.datdang.snakeblock.domain.model.*

class SnakeBlockGameEngine {
    companion object {
        const val GRID_SIZE = 10
    }

    private var board: Array<Array<CellType>> = Array(GRID_SIZE) { Array(GRID_SIZE) { CellType.EMPTY } }
    private var snakePath: MutableList<Coordinate> = mutableListOf()
    private var totalRedBlocks: Int = 0
    private var redBlocksFilled: Int = 0
    private val gameStateHistory: MutableList<GameState> = mutableListOf()

    fun setupLevel(levelData: List<Triple<Int, Int, CellType>>) {
        // Reset board
        board = Array(GRID_SIZE) { Array(GRID_SIZE) { CellType.EMPTY } }
        snakePath.clear()
        totalRedBlocks = 0
        redBlocksFilled = 0
        gameStateHistory.clear()

        // First pass: setup all red blocks
        for ((row, col, type) in levelData) {
            when (type) {
                CellType.RED_BLOCK -> {
                    board[row][col] = CellType.RED_BLOCK
                    totalRedBlocks++
                }
                else -> {
                    // Ignore other types in first pass
                }
            }
        }

        // Second pass: setup snake head (must be on a red block)
        for ((row, col, type) in levelData) {
            when (type) {
                CellType.SNAKE_HEAD -> {
                    // Snake head must start on a red block
                    if (board[row][col] == CellType.RED_BLOCK) {
                        board[row][col] = CellType.SNAKE_HEAD
                        snakePath.add(Coordinate(row, col))
                        redBlocksFilled++ // Count this red block as filled
                    }
                }
                else -> {
                    // Already handled in first pass
                }
            }
        }

        // Save initial state
        saveCurrentState()
    }

    fun moveSnake(targetCoordinate: Coordinate): MoveResult {
        val currentHead = snakePath.lastOrNull() ?: return MoveResult.Error("No snake head found")

        // 1. Check valid move (only 1 cell, not diagonal)
        val dr = kotlin.math.abs(targetCoordinate.row - currentHead.row)
        val dc = kotlin.math.abs(targetCoordinate.col - currentHead.col)
        if (dr + dc != 1) {
            return MoveResult.Error("Invalid move: Not adjacent to snake head.")
        }

        // 2. Check out of bounds
        if (targetCoordinate.row !in 0 until GRID_SIZE || targetCoordinate.col !in 0 until GRID_SIZE) {
            return MoveResult.Error("Invalid move: Out of bounds.")
        }

        // 3. Check moving into snake body
        if (snakePath.contains(targetCoordinate)) {
            return MoveResult.Error("Invalid move: Cannot go into snake body.")
        }

        // 4. Check can only move to red blocks
        if (board[targetCoordinate.row][targetCoordinate.col] != CellType.RED_BLOCK) {
            return MoveResult.Error("Invalid move: Can only move to red blocks.")
        }

        // Save state before moving
        saveCurrentState()

        // 5. Valid move:
        // 5.1. Update old snake head cell to snake body
        board[currentHead.row][currentHead.col] = CellType.SNAKE_BODY

        // 5.2. Update snake path (add target cell to end)
        snakePath.add(targetCoordinate)

        // 5.3. Update target cell to snake head and count filled red block
        board[targetCoordinate.row][targetCoordinate.col] = CellType.SNAKE_HEAD
        redBlocksFilled++ // Filled one more red block

        return MoveResult.Success
    }

    fun checkWin(): Boolean {
        return redBlocksFilled == totalRedBlocks
    }

    fun undoMove(): Boolean {
        if (gameStateHistory.size <= 1) return false // Cannot undo if only initial state exists

        // Remove current state
        gameStateHistory.removeLastOrNull()
        
        // Get previous state
        val previousState = gameStateHistory.lastOrNull() ?: return false

        // Restore state
        board = previousState.board.map { it.clone() }.toTypedArray()
        snakePath.clear()
        snakePath.addAll(previousState.snakePath)
        totalRedBlocks = previousState.totalRedBlocks
        redBlocksFilled = previousState.redBlocksFilled

        return true
    }

    fun getCurrentGameState(): GameState {
        return GameState(
            board = board.map { it.clone() }.toTypedArray(),
            snakePath = snakePath.toList(),
            totalRedBlocks = totalRedBlocks,
            redBlocksFilled = redBlocksFilled,
            isGameWon = checkWin()
        )
    }

    private fun saveCurrentState() {
        val currentState = getCurrentGameState()
        gameStateHistory.add(currentState)
    }

    fun canUndo(): Boolean {
        return gameStateHistory.size > 1
    }

    fun getSnakeHead(): Coordinate? {
        return snakePath.lastOrNull()
    }

    fun getSnakePath(): List<Coordinate> {
        return snakePath.toList()
    }

    fun getBoard(): Array<Array<CellType>> {
        return board.map { it.clone() }.toTypedArray()
    }

    fun getTotalRedBlocks(): Int = totalRedBlocks
    fun getRedBlocksFilled(): Int = redBlocksFilled
}