package com.centr.pro.dp.sensor

import com.centr.pro.dp.core.DeviceMotionResult
import com.centr.pro.dp.core.Info

class LightSensorInfo : Info {
    override suspend fun collect(vararg args: Any?): String {
        return try {
            val deviceMotionResult = args[0] as DeviceMotionResult
            val score = deviceMotionResult.lightScore ?: "undefined"
            "LIGHT[$score]"
        } catch (e: Throwable) {
            "LIGHT[undefined]"
        }
    }
}
