package com.centr.viewmodel.model

data class MenuUiState(
    val coins: Long = 50_000L,
    val level: Int = 1,
    val levelProgress: Float = 0f,
    val dailyBonusDay: Int = 0,
    val canClaimDailyBonus: Boolean = true,
    val giftBoxVisible: Boolean = true,
    val giftBoxCooldownEnd: Long = 0L,
    val leaderboard: List<LeaderboardEntry> = emptyList()
)

