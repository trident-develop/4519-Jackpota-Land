package com.centr.ui.screens

import android.content.Context
import android.webkit.WebSettings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.centr.sound.Msadf
import com.centr.viewmodel.Vasddf
import com.centr.pro.dp.system.nsdf
import com.centr.pro.dp.network.cua
import com.centr.navigation.basdf
import com.centr.navigation.NavigationStore.navigate
import com.centr.navigation.ScreenMove
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun StartMenu(onMove: () -> Unit) {
    LaunchedEffect(Unit) {
        onMove()
    }
}

suspend fun vbasdf(
    context: Context,
    startD: Vasddf,
) {
    try {
        var firebaseId = ""

        firebaseId = try {
            Firebase.analytics.appInstanceId.await().toString()
        } catch (e: Exception) {
            ""
        }

        withContext(Dispatchers.Main) {
            val wv = startD.oneView
            val u = "${Msadf().long}?" + nsdf(
                startD.ref, firebaseId, context, startD
            )
            val map = basdf(context)
            val updU = u + "&" + map.toQueryParams()
            if (updU.isNotEmpty()) {
                try {
                    val assdf = WebSettings.getDefaultUserAgent(context)
                    wv.loadUrl(updU, mapOf(cua to assdf))
                } catch (e: Exception) {
                    navigate(ScreenMove.Move)
                }
            } else {
                navigate(ScreenMove.Move)
            }
        }
    } catch (e: Exception) {
        navigate(ScreenMove.Move)
    }
}

private fun Map<String, String?>.toQueryParams(): String {
    return this
        .filterValues { !it.isNullOrEmpty() }
        .map { (key, value) ->
            val encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8.toString())
            val encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
            "$encodedKey=$encodedValue"
        }
        .joinToString("&")
}
