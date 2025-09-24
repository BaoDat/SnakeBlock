package com.datdang.snakeblock.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datdang.snakeblock.ui.theme.*
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MenuScreen(
    onNavigateToSnakeGame: () -> Unit,
    onNavigateToDailyChallenge: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToStore: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel()
) {
    // Collect navigation events
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is MenuNavigationEvent.NavigateToSnakeGame -> onNavigateToSnakeGame()
                is MenuNavigationEvent.NavigateToSettings -> onNavigateToSettings()
                is MenuNavigationEvent.NavigateToDailyChallenge -> onNavigateToDailyChallenge()
                is MenuNavigationEvent.NavigateToStore -> onNavigateToStore()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SnakeBlockBackground)
    ) {
        // Top bar with home and settings icons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {  viewModel.onEvent(MenuEvent.StoreClicked) }
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = SnakeBlockWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            IconButton(
                onClick = { viewModel.onEvent(MenuEvent.SettingsClicked) }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = SnakeBlockWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo section with colored blocks
            SnakeBlockLogo()
            
            Spacer(modifier = Modifier.height(80.dp))
            
            // Play button
            Button(
                onClick = { viewModel.onEvent(MenuEvent.SnakeGameClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SnakeBlockBlue
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Play",
                    color = SnakeBlockWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Daily Challenge button
            Button(
                onClick = { viewModel.onEvent(MenuEvent.DailyChallengeClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SnakeBlockYellow
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Daily Challenge",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Store button (smaller)
            OutlinedButton(
                onClick = { viewModel.onEvent(MenuEvent.StoreClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = SnakeBlockWhite
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Store",
                    color = SnakeBlockWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun SnakeBlockLogo() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Colored blocks arrangement
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Left side blocks
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ColoredBlock(SnakeBlockLightBlue, 24.dp)
                ColoredBlock(SnakeBlockLightBlue, 24.dp)
            }
            
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ColoredBlock(SnakeBlockGreen, 24.dp)
                ColoredBlock(SnakeBlockGreen, 24.dp)
                ColoredBlock(SnakeBlockPurple, 24.dp)
            }
            
            // Right side - snake-like arrangement
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ColoredBlock(SnakeBlockRed, 24.dp)
                ColoredBlock(SnakeBlockOrange, 24.dp)
                ColoredBlock(SnakeBlockYellow, 24.dp)
                ColoredBlock(SnakeBlockYellow, 24.dp)
                ColoredBlock(SnakeBlockYellow, 24.dp)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // SNAKE BLOX text
        Text(
            text = "SNAKE\nBLOX",
            color = SnakeBlockWhite,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )
    }
}

@Composable
private fun ColoredBlock(
    color: Color,
    size: Dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(4.dp))
            .background(color)
    )
}