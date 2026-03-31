package com.centr.pro.dp.core

fun interface Info {
    suspend fun collect(vararg args: Any?): String
}