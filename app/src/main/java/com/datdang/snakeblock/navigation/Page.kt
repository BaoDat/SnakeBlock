package com.datdang.snakeblock.navigation

import kotlinx.serialization.Serializable
import kotlin.jvm.javaClass

sealed class Page {
    @Serializable
    data object Splash : Page()

    @Serializable
    data object NavigationBar : Page()

    @Serializable
    data object Menu : Page()

    @Serializable
    data object RedLightGreenLight : Page()

    @Serializable
    data object JumpRope : Page()

    @Serializable
    data object GlassSteppingStones : Page()

    @Serializable
    data object RunnerSprint : Page()

    @Serializable
    data object DalgonaCandy : Page()

    @Serializable
    data object SnakeGame : Page()

    @Serializable
    data object Settings : Page()

    @Serializable
    data object Store : Page()

    @Serializable
    data object DailyChallenge : Page()

    @Serializable
    data object BalancePathGame : Page()
}

sealed class Graph {
    @Serializable
    data object Main : Graph()
}

fun Page.route(): String? {
    return this.javaClass.canonicalName
}