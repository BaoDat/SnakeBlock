package com.datdang.snakeblock.navigation

import androidx.navigation.NavHostController

class MainRouter(
    private val mainNavController: NavHostController
) {

    fun navigateToMenu() {
        mainNavController.navigate(Page.Menu) {
            popUpTo(Page.Splash) {
                inclusive = true
            }
        }
    }

    fun navigateToRedLightGreenLight() {
        mainNavController.navigate(Page.RedLightGreenLight)
    }

    fun navigateToJumpRope() {
        mainNavController.navigate(Page.JumpRope)
    }

    fun navigateToGlassSteppingStones() {
        mainNavController.navigate(Page.GlassSteppingStones)
    }

    fun navigateToRunnerSprint() {
        mainNavController.navigate(Page.RunnerSprint)
    }

    fun navigateToDalgonaCandy() {
        mainNavController.navigate(Page.DalgonaCandy)
    }

    fun navigateToSnakeGame() {
        mainNavController.navigate(Page.SnakeGame)
    }

    fun navigateToBalancePathGame() {
        mainNavController.navigate(Page.BalancePathGame)
    }

    fun navigateToSettings() {
        mainNavController.navigate(Page.Settings)
    }

    fun navigateToStore() {
        mainNavController.navigate(Page.Store)
    }

    fun navigateToDailyChallenge() {
        mainNavController.navigate(Page.DailyChallenge)
    }

    fun navigateBack() {
        if (mainNavController.previousBackStackEntry != null) {
            mainNavController.popBackStack()
        }
    }

    fun navigateToMenuFromGame() {
        mainNavController.navigate(Page.Menu) {
            popUpTo(Page.Menu) {
                inclusive = true
            }
        }
    }

    fun canNavigateBack(): Boolean {
        return mainNavController.previousBackStackEntry != null
    }

    fun getCurrentRoute(): String? {
        return mainNavController.currentBackStackEntry?.destination?.route
    }
}