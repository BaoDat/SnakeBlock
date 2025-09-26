package com.datdang.snakeblock.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.datdang.snakeblock.domain.model.CellType
import com.datdang.snakeblock.domain.model.Coordinate
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnakeGameScreen(
    viewModel: GameViewModel = hiltViewModel()
) {
    val gameState by viewModel.gameState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val canUndo by viewModel.canUndo.collectAsState()
    val isGameWon by viewModel.isGameWon.collectAsState()
    val currentLevel by viewModel.currentLevel.collectAsState()
    val moveCount by viewModel.moveCount.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startLevel(1)
    }

    // Show win dialog
    if (isGameWon) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Congratulations!") },
            text = { 
                Column {
                    Text("You completed Level $currentLevel!")
                    Text("Moves: $moveCount")
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.nextLevel() }) {
                    Text("Next Level")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.resetGame() }) {
                    Text("Restart")
                }
            }
        )
    }

    // Show error message
    errorMessage?.let { message ->
        LaunchedEffect(message) {
            kotlinx.coroutines.delay(2000)
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Game info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Level $currentLevel",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Moves: $moveCount",
                    fontSize = 14.sp
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Red Blocks: ${gameState.redBlocksFilled}/${gameState.totalRedBlocks}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { viewModel.undoMove() },
                enabled = canUndo
            ) {
                Text("Undo")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Error message
        errorMessage?.let { message ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Game board
        GameBoard(
            gameState = gameState,
            onMove = { targetCoordinate ->
                viewModel.moveSnake(targetCoordinate)
            },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Control buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { viewModel.startLevel(1) }) {
                Text("Level 1")
            }
            Button(onClick = { viewModel.startLevel(2) }) {
                Text("Level 2")
            }
            Button(onClick = { viewModel.startLevel(3) }) {
                Text("Level 3")
            }
            Button(onClick = { viewModel.resetGame() }) {
                Text("Reset")
            }
        }
    }
}