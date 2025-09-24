package com.datdang.snakeblock.ui.menu

sealed class MenuEvent {
    data object SnakeGameClicked : MenuEvent()
    data object SettingsClicked : MenuEvent()
    data object DailyChallengeClicked : MenuEvent()
    data object StoreClicked : MenuEvent()
}