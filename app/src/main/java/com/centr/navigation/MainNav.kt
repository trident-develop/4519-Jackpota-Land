package com.centr.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.centr.data.GameTheme
import com.centr.sound.SoundManager
import com.centr.ui.screens.LeaderboardScreen
import com.centr.ui.screens.MenuScreen
import com.centr.ui.screens.PrivacyScreen
import com.centr.ui.screens.SlotGameScreen
import com.centr.viewmodel.MenuViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    soundManager: SoundManager,
    menuViewModel: MenuViewModel = viewModel()
) {
    NavHost(navController = navController, startDestination = Routes.MENU) {
        composable(Routes.MENU) {
            MenuScreen(
                viewModel = menuViewModel,
                onPlayGame = { theme ->
                    navController.navigate(Routes.gameRoute(theme.ordinal))
                },
                onLeaderboard = {
                    navController.navigate(Routes.LEADERBOARD)
                },
                onPrivacy = {
                    navController.navigate(Routes.PRIVACY)
                }
            )
        }

        composable(Routes.GAME) { backStackEntry ->
            val themeId = backStackEntry.arguments?.getString("themeId")?.toIntOrNull() ?: 0
            val theme = GameTheme.entries.getOrElse(themeId) { GameTheme.AZTEC }
            SlotGameScreen(
                theme = theme,
                soundManager = soundManager,
                onBack = {
                    menuViewModel.refreshState()
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.LEADERBOARD) {
            LeaderboardScreen(
                viewModel = menuViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.PRIVACY) {
            PrivacyScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
