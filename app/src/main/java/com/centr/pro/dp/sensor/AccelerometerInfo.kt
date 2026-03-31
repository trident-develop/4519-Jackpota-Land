package com.centr.pro.dp.sensor

import com.centr.pro.dp.core.Info
import com.centr.pro.dp.core.DeviceMotionResult

class AccelerometerInfo : Info {
    override suspend fun collect(vararg args: Any?): String {
        return try {
            val deviceMotionResult = args[0] as DeviceMotionResult
            val score = deviceMotionResult.accelScore ?: "undefined"
            "ACC[$score]"
        } catch (e: Throwable) {
            "ACC[undefined]"
        }
    }
}