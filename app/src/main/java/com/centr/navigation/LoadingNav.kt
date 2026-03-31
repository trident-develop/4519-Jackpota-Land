package com.centr.navigation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.centr.LoadingActivity
import com.centr.MainActivity
import com.centr.viewmodel.Vasddf
import com.centr.ui.screens.ConnectScreen
import com.centr.ui.screens.LoadingScreen
import com.centr.ui.screens.StartMenu

@SuppressLint("ContextCastToActivity")
@Composable
fun LoadingGraph(
    com: ComponentActivity,
    factory: Vasddf
) {

    val navController = rememberNavController()
    val context = LocalContext.current as LoadingActivity

    NavHost(
        navController = navController,
        startDestination = Routes.LOADING
    ) {
        composable(Routes.LOADING) {
            val screen by NavigationStore.currentScreen.collectAsState()
            when (screen) {
                ScreenMove.Loading -> LoadingScreen(com, factory)
                ScreenMove.Move -> StartMenu {
                    context.startActivity(Intent(context, MainActivity::class.java))
                    context.finish()
                }
                ScreenMove.NoConnection -> ConnectScreen()
                else -> {}
            }
        }

        composable(Routes.CONNECT) {
            ConnectScreen()
        }
    }
}