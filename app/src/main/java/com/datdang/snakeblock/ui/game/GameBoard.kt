package com.datdang.snakeblock.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.datdang.snakeblock.domain.game.SnakeBlockGameEngine
import com.datdang.snakeblock.domain.model.CellType
import com.datdang.snakeblock.domain.model.Coordinate
import com.datdang.snakeblock.domain.model.GameState
import kotlin.math.abs

@Composable
fun GameBoard(
    gameState: GameState,
    onMove: (Coordinate) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var dragStart by remember { mutableStateOf<Offset?>(null) }
    var lastMoveCoord by remember { mutableStateOf<Coordinate?>(null) }
    var boardSize by remember { mutableStateOf(0.dp) }

    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f)
    ) {
        boardSize = minOf(this.maxWidth, this.maxHeight)
        val cellSize = boardSize / SnakeBlockGameEngine.GRID_SIZE

        Column(
            modifier = Modifier
                .size(boardSize)
                .pointerInput(gameState.snakePath) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val startCoord = offsetToCoordinate(offset, cellSize.toPx())
                            val snakeHead = gameState.snakePath.lastOrNull()
                            
                            // Only start drag if touching the snake head
                            if (startCoord == snakeHead) {
                                dragStart = offset
                                lastMoveCoord = startCoord
                            }
                        },
                        onDragEnd = {
                            dragStart = null
                            lastMoveCoord = null
                        },
                        onDrag = { change, _ ->
                            dragStart?.let { _ ->
                                val currentCoord = offsetToCoordinate(change.position, cellSize.toPx())
                                val snakeHead = gameState.snakePath.lastOrNull()
                                
                                // Check if we moved to a new cell and it's adjacent to current snake head
                                if (currentCoord != lastMoveCoord && currentCoord != snakeHead && snakeHead != null) {
                                    val dr = abs(currentCoord.row - snakeHead.row)
                                    val dc = abs(currentCoord.col - snakeHead.col)
                                    
                                    // Valid adjacent move
                                    if (dr + dc == 1) {
                                        onMove(currentCoord)
                                        lastMoveCoord = currentCoord
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            repeat(SnakeBlockGameEngine.GRID_SIZE) { row ->
                Row {
                    repeat(SnakeBlockGameEngine.GRID_SIZE) { col ->
                        GameCell(
                            cellType = gameState.board[row][col],
                            modifier = Modifier.size(cellSize)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GameCell(
    cellType: CellType,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (cellType) {
        CellType.EMPTY -> Color.LightGray
        CellType.RED_BLOCK -> Color.Red
        CellType.SNAKE_HEAD -> Color.Green
        CellType.SNAKE_BODY -> Color.Blue
    }

    Box(
        modifier = modifier
            .background(backgroundColor)
            .border(1.dp, Color.Black)
            .clip(RoundedCornerShape(2.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Add eyes for Snake Head
        if (cellType == CellType.SNAKE_HEAD) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val cellSize = size.width
                val eyeRadius = cellSize * 0.08f
                val eyeOffsetX = cellSize * 0.15f
                val eyeOffsetY = cellSize * 0.15f
                
                // Left eye
                drawCircle(
                    color = Color.White,
                    radius = eyeRadius,
                    center = Offset(
                        x = cellSize / 2 - eyeOffsetX,
                        y = cellSize / 2 - eyeOffsetY
                    )
                )
                
                // Right eye
                drawCircle(
                    color = Color.White,
                    radius = eyeRadius,
                    center = Offset(
                        x = cellSize / 2 + eyeOffsetX,
                        y = cellSize / 2 - eyeOffsetY
                    )
                )
            }
        }
    }
}

private fun offsetToCoordinate(offset: Offset, cellSize: Float): Coordinate {
    val col = (offset.x / cellSize).toInt()
    val row = (offset.y / cellSize).toInt()
    return Coordinate(
        row = row.coerceIn(0, SnakeBlockGameEngine.GRID_SIZE - 1),
        col = col.coerceIn(0, SnakeBlockGameEngine.GRID_SIZE - 1)
    )
}