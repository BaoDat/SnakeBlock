package com.datdang.snakeblock.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor() : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<MenuNavigationEvent>()
    val navigationEvent: SharedFlow<MenuNavigationEvent> = _navigationEvent.asSharedFlow()

    fun onEvent(event: MenuEvent) {
        when (event) {
            is MenuEvent.SnakeGameClicked -> {
                viewModelScope.launch {
                    _navigationEvent.emit(MenuNavigationEvent.NavigateToSnakeGame)
                }
            }
            is MenuEvent.SettingsClicked -> {
                viewModelScope.launch {
                    _navigationEvent.emit(MenuNavigationEvent.NavigateToSettings)
                }
            }
            is MenuEvent.DailyChallengeClicked -> {
                viewModelScope.launch {
                    _navigationEvent.emit(MenuNavigationEvent.NavigateToDailyChallenge)
                }
            }
            is MenuEvent.StoreClicked -> {
                viewModelScope.launch {
                    _navigationEvent.emit(MenuNavigationEvent.NavigateToStore)
                }
            }
        }
    }
}

sealed class MenuNavigationEvent {
    data object NavigateToSnakeGame : MenuNavigationEvent()
    data object NavigateToSettings : MenuNavigationEvent()
    data object NavigateToDailyChallenge : MenuNavigationEvent()
    data object NavigateToStore : MenuNavigationEvent()
}