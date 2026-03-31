package com.centr.navigation

import android.content.Context
import com.centr.pro.dp.core.DevicePropertiesResult

val ep4 = "i7g3tnu9xbz2"
val ep5 = "0nearlb25l8bbk"
val ep8 = "i9udvswtivrgoh4f36"
val ep9 = "yr5lzf8xop98v4"
val ep11 = "75x57d9lqqqe58bfa"
val ep12 = "nea8s7kmrvnoj8i3v5l"

suspend fun basdf(context: Context) : Map<String, String> {
        val dev = DevicePropertiesResult.create(context)
        val map = mutableMapOf<String, String>()

        map[ep4] = dev.getX4()
        map[ep5] = dev.getX5()
        map[ep8] = dev.getX8()
        map[ep9] = dev.getX9()
        map[ep11] = dev.getS28()
        map[ep12] = dev.getS30()

        return map
}
