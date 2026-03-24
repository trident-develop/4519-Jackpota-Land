package com.centr.navigation
object Routes {
    const val MENU = "menu"
    const val GAME = "game/{themeId}"
    const val LEADERBOARD = "leaderboard"
    const val PRIVACY = "privacy"
    const val LOADING = "loading"
    const val CONNECT = "connect"
    fun gameRoute(themeId: Int) = "game/$themeId"
}