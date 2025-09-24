package com.datdang.snakeblock.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.datdang.snakeblock.ui.splash.SplashScreen
import com.datdang.snakeblock.ui.menu.MenuScreen
import com.datdang.snakeblock.ui.settings.SettingsScreen
import com.datdang.snakeblock.ui.store.StoreScreen
import com.datdang.snakeblock.ui.dailychallenge.DailyChallengeScreen
import com.datdang.snakeblock.ui.game.SnakeGameScreen
import com.datdang.snakeblock.util.composableHorizontalSlide

@Composable
fun MainGraph(
    mainNavController: NavHostController
) {
    val router = remember { MainRouter(mainNavController) }
    val context = androidx.compose.ui.platform.LocalContext.current


    NavHost(
        navController = mainNavController,
        startDestination = Page.Splash,
        route = Graph.Main::class
    ) {
        composableHorizontalSlide<Page.Splash> {
            SplashScreen(
                onSplashComplete = {
                    router.navigateToMenu()
                }
            )
        }

        composableHorizontalSlide<Page.Menu> {
            MenuScreen(
                onNavigateToSnakeGame = {
                    router.navigateToSnakeGame()
                },
                onNavigateToDailyChallenge = {
                    router.navigateToDailyChallenge()
                },
                onNavigateToSettings = {
                    router.navigateToSettings()
                },
                onNavigateToStore = {
                    router.navigateToStore()
                }
            )
        }

        composableHorizontalSlide<Page.SnakeGame> {
            SnakeGameScreen()
        }

        composableHorizontalSlide<Page.Store> {
            StoreScreen(
                onBackClick = {
                    router.navigateBack()
                }
            )
        }

        composableHorizontalSlide<Page.DailyChallenge> {
            DailyChallengeScreen(
                onBackClick = {
                    router.navigateBack()
                }
            )
        }

        composableHorizontalSlide<Page.Settings> {
            SettingsScreen(
                onBackClick = {
                    router.navigateBack()
                }
            )
        }
    }
}