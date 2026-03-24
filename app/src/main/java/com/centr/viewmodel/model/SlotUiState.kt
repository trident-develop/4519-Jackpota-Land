package com.centr.viewmodel.model

import com.centr.data.WinLine

data class SlotUiState(
    val grid: List<List<Int>> = emptyList(),
    val displayGrid: List<List<Int>> = emptyList(),
    val spinning: Boolean = false,
    val columnStopped: List<Boolean> = emptyList(),
    val coins: Long = 50_000L,
    val bet: Long = 1_000L,
    val level: Int = 1,
    val levelProgress: Float = 0f,
    val winLines: List<WinLine> = emptyList(),
    val totalWin: Long = 0L,
    val lastWin: Long = 0L,
    val showWinScreen: Boolean = false,
    val showLevelUp: Boolean = false,
    val autoSpin: Boolean = false,
    val paused: Boolean = false,
    val speedLevel: Int = 1,
    val maxBet: Long = 5_000L,
    // Reel animation data
    val reelStrips: List<List<Int>> = emptyList(), // per column: long strip of symbol indices
    val spinTrigger: Int = 0 // incremented to trigger UI animation
)