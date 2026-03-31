package com.centr.pro.dp.sensor

import com.centr.pro.dp.core.DeviceMotionResult
import com.centr.pro.dp.core.Info

class ProximitySensorInfo : Info {
    override suspend fun collect(vararg args: Any?): String {
        return try {
            val deviceMotionResult = args[0] as DeviceMotionResult
            val score = deviceMotionResult.proxScore ?: "undefined"
            "PROXIMITY[$score]"
        } catch (e: Throwable) {
            "PROXIMITY[undefined]"
        }
    }
}
