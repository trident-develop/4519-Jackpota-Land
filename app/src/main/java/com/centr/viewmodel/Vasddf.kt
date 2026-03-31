package com.centr.viewmodel

import android.content.pm.PackageInfo
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import com.centr.viewmodel.model.Pvfas
import com.centr.pro.dp.sensor.hasf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Locale

class Vasddf(val activity: ComponentActivity) {
    var oneView: Vasdfa = Vasdfa(activity, Pvfas(activity))
    lateinit var ref: String
    var status: String

    init {
        try {
            status =
        Settings.Global.getString(activity.contentResolver, Settings.Global.ADB_ENABLED) ?: "0"
//                "0"
        } catch (e: Exception) {
            status = "1"
        }
    }

    fun getOneV(): Vasdfa {
        return oneView
    }

    suspend fun getR() {
        withContext(Dispatchers.IO) {
            ref = hasf(activity)
        }
    }

    fun getTime(): String {
        val packageInfo: PackageInfo =
            activity.packageManager.getPackageInfo(activity.packageName, 0)
        val time = packageInfo.firstInstallTime.toString()
        return time
    }

    fun getDev(): String {
        val device =
            Build.BRAND.replaceFirstChar { it.titlecase(Locale.getDefault()) } + " " + Build.MODEL
        val encoded2 = URLEncoder.encode(device, StandardCharsets.UTF_8.toString())
        return encoded2
    }
}
