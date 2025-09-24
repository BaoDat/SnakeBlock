package com.datdang.snakeblock.ui.splash

import com.datdang.snakeblock.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import androidx.compose.ui.unit.dp
import com.datdang.snakeblock.ui.theme.SnakeBlockBackground
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

private const val SPLASH_DELAY_MILLIS: Long = 8500
private const val LOTTIE_WIDTH_DP = 300
private const val LOTTIE_HEIGHT_DP = 200

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    // Load the Lottie animation from res/raw/splash_animation.json
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )
    val systemUiController = rememberSystemUiController()
    DisposableEffect(Unit) {
        systemUiController.isStatusBarVisible = false
        onDispose {
            // Hiện lại status bar khi rời SplashScreen (nếu muốn)
            systemUiController.isStatusBarVisible = true
        }
    }

    // Start splash sequence and navigate after delay
    LaunchedEffect(Unit) {
        delay(SPLASH_DELAY_MILLIS)
        onSplashComplete()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SnakeBlockBackground)
    ) {
        // Set background image
        // Lottie animation ở top
        if (composition != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .width(LOTTIE_WIDTH_DP.dp)
                        .height(LOTTIE_HEIGHT_DP.dp)
                )
            }
        }
    }
}
