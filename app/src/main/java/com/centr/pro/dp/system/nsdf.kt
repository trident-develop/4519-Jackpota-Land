package com.centr.pro.dp.system

import android.content.Context
import android.os.Build
import com.centr.viewmodel.Vasddf
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder

val ep1 = "71b4d7i4j1"
val ep2 = "zq7mcafxungyb224pmd"
val ep3 = "g86egscnniouj"
val ep10 = "rrm03fevcsp1qfwa"
val gk = "e1qko05n6si16swm"
val rk = "cbgm9u1urqfdu8p3u"
val ei = "ffk0183qder3ecmzzwt"

suspend fun nsdf(
    raw: String,
    id: String,
    context: Context,
    startD: Vasddf
): String {
    val encoded = URLEncoder.encode(raw, Charsets.UTF_8.name())

    val params = buildMap {
        put(gk, runAfter(context))
        put(rk, encoded)
        put(ei, id)
        put(ep1, startD.getTime())
        put(ep2, startD.status)
        put(ep3, startD.getDev())
        put(ep10, runClose(context).toString())
    }

    return params
        .filterValues { !it.isNullOrBlank() }
        .entries
        .joinToString("&") { (key, value) -> "$key=$value" }
}

private fun runClose(context: Context): Int? = runCatching {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val packageManager = context.packageManager
        val sourceInfo = packageManager.getInstallSourceInfo(context.packageName)
        sourceInfo.packageSource
    } else null
}.getOrNull()

private suspend fun runAfter(context: Context): String = withContext(Dispatchers.IO) {
    try {
        val info = AdvertisingIdClient.getAdvertisingIdInfo(context)
        if (!info.isLimitAdTrackingEnabled) {
            info.id ?: "00000000-0000-0000-0000-000000000000"
        } else {
            "00000000-0000-0000-0000-000000000000"
        }
    } catch (_: Exception) {
        "00000000-0000-0000-0000-000000000000"
    }
}
