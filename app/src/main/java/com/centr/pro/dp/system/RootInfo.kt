package com.centr.pro.dp.system

import com.centr.pro.dp.core.Info
import java.io.File

class RootInfo : Info {
    override suspend fun collect(vararg args: Any?): String {
        val rootBinariesPaths = listOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/bin/magiskpolicy"
        )

        val rootBinaries = rootBinariesPaths.any { path ->
            try {
                File(path).exists()
            } catch (_: Throwable) {
                false
            }
        }

        val detectedInt = if(rootBinaries) 1 else 0

        return "ROOT[${detectedInt}]"
    }
}