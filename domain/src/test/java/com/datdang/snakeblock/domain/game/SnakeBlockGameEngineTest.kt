package com.datdang.snakeblock.domain.game

import com.datdang.snakeblock.domain.model.CellType
import com.datdang.snakeblock.domain.model.Coordinate
import com.datdang.snakeblock.domain.model.MoveResult
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class SnakeBlockGameEngineTest {

    private lateinit var gameEngine: SnakeBlockGameEngine

    @Before
    fun setup() {
        gameEngine = SnakeBlockGameEngine()
    }

    @Test
    fun `test level setup`() {
        val levelData = listOf(
            Triple(2, 2, CellType.RED_BLOCK),
            Triple(2, 3, CellType.RED_BLOCK),
            Triple(0, 0, CellType.SNAKE_HEAD)
        )

        gameEngine.setupLevel(levelData)

        val gameState = gameEngine.getCurrentGameState()
        assertEquals(2, gameState.totalRedBlocks)
        assertEquals(0, gameState.redBlocksFilled)
        assertEquals(1, gameState.snakePath.size)
        assertEquals(Coordinate(0, 0), gameState.snakePath.first())
        assertEquals(CellType.SNAKE_HEAD, gameState.board[0][0])
        assertEquals(CellType.RED_BLOCK, gameState.board[2][2])
        assertEquals(CellType.RED_BLOCK, gameState.board[2][3])
    }

    @Test
    fun `test valid move to empty cell`() {
        val levelData = listOf(
            Triple(0, 0, CellType.SNAKE_HEAD)
        )
        gameEngine.setupLevel(levelData)

        val result = gameEngine.moveSnake(Coordinate(0, 1))

        assertTrue(result is MoveResult.Success)
        val gameState = gameEngine.getCurrentGameState()
        assertEquals(2, gameState.snakePath.size)
        assertEquals(Coordinate(0, 1), gameState.snakePath.last())
        assertEquals(CellType.SNAKE_HEAD, gameState.board[0][1])
        assertEquals(CellType.SNAKE_BODY, gameState.board[0][0])
    }

    @Test
    fun `test valid move to red block`() {
        val levelData = listOf(
            Triple(0, 1, CellType.RED_BLOCK),
            Triple(0, 0, CellType.SNAKE_HEAD)
        )
        gameEngine.setupLevel(levelData)

        val result = gameEngine.moveSnake(Coordinate(0, 1))

        assertTrue(result is MoveResult.Success)
        val gameState = gameEngine.getCurrentGameState()
        assertEquals(1, gameState.redBlocksFilled)
        assertEquals(CellType.SNAKE_HEAD, gameState.board[0][1])
    }

    @Test
    fun `test invalid move - diagonal`() {
        val levelData = listOf(
            Triple(0, 0, CellType.SNAKE_HEAD)
        )
        gameEngine.setupLevel(levelData)

        val result = gameEngine.moveSnake(Coordinate(1, 1))

        assertTrue(result is MoveResult.Error)
        assertTrue((result as MoveResult.Error).message.contains("diagonal"))
    }

    @Test
    fun `test invalid move - out of bounds`() {
        val levelData = listOf(
            Triple(0, 0, CellType.SNAKE_HEAD)
        )
        gameEngine.setupLevel(levelData)

        val result = gameEngine.moveSnake(Coordinate(-1, 0))

        assertTrue(result is MoveResult.Error)
        assertTrue((result as MoveResult.Error).message.contains("bounds"))
    }

    @Test
    fun `test invalid move - into snake body`() {
        val levelData = listOf(
            Triple(0, 0, CellType.SNAKE_HEAD)
        )
        gameEngine.setupLevel(levelData)

        // Move to create snake body
        gameEngine.moveSnake(Coordinate(0, 1))
        gameEngine.moveSnake(Coordinate(1, 1))

        // Try to move back into snake body
        val result = gameEngine.moveSnake(Coordinate(0, 1))

        assertTrue(result is MoveResult.Error)
        assertTrue((result as MoveResult.Error).message.contains("snake body"))
    }

    @Test
    fun `test win condition`() {
        val levelData = listOf(
            Triple(0, 1, CellType.RED_BLOCK),
            Triple(0, 0, CellType.SNAKE_HEAD)
        )
        gameEngine.setupLevel(levelData)

        assertFalse(gameEngine.checkWin())

        gameEngine.moveSnake(Coordinate(0, 1))

        assertTrue(gameEngine.checkWin())
    }

    @Test
    fun `test undo functionality`() {
        val levelData = listOf(
            Triple(0, 0, CellType.SNAKE_HEAD)
        )
        gameEngine.setupLevel(levelData)

        // Initial state
        assertFalse(gameEngine.canUndo())

        // Make a move
        gameEngine.moveSnake(Coordinate(0, 1))
        assertTrue(gameEngine.canUndo())

        val stateAfterMove = gameEngine.getCurrentGameState()
        assertEquals(2, stateAfterMove.snakePath.size)

        // Undo the move
        assertTrue(gameEngine.undoMove())
        val stateAfterUndo = gameEngine.getCurrentGameState()
        assertEquals(1, stateAfterUndo.snakePath.size)
        assertEquals(Coordinate(0, 0), stateAfterUndo.snakePath.first())
    }
}