package com.centr.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object NavigationStore {

    private val _currentScreen = MutableStateFlow<ScreenMove>(ScreenMove.Loading)
    val currentScreen: StateFlow<ScreenMove> = _currentScreen

    fun navigate(screen: ScreenMove) {
        _currentScreen.value = screen
    }
}

sealed class ScreenMove {
    data object Splash : ScreenMove()
    data object Menu : ScreenMove()
    data object Move : ScreenMove()
    data object NoConnection : ScreenMove()
    data object Game : ScreenMove()
    data object Settings : ScreenMove()
    data object Loading : ScreenMove()
    data class Details(val id: String) : ScreenMove()
}
