package com.datdang.snakeblock.ui.levelselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datdang.snakeblock.domain.model.Level
import com.datdang.snakeblock.domain.usecase.GetAllLevelsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LevelSelectionViewModel @Inject constructor(
    private val getAllLevelsUseCase: GetAllLevelsUseCase
) : ViewModel() {

    private val _levels = MutableStateFlow<List<Level>>(emptyList())
    val levels: StateFlow<List<Level>> = _levels.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadLevels()
    }

    private fun loadLevels() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                getAllLevelsUseCase().collect { levelList ->
                    _levels.value = levelList
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load levels: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun refreshLevels() {
        loadLevels()
    }
}