package com.centr.pro.dp.sensor

import com.centr.pro.dp.core.Info
import com.centr.pro.dp.core.DeviceMotionResult

class MagnetometerInfo : Info {
    override suspend fun collect(vararg args: Any?): String {
        return try {
            val deviceMotionResult = args[0] as DeviceMotionResult
            val score = deviceMotionResult.magScore ?: "undefined"
            "MAGN[$score]"
        } catch (e: Throwable) {
            "MAGN[undefined]"
        }
    }
}
