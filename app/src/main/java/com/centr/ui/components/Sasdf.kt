package com.centr.ui.components

import android.content.Context
import com.centr.data.Save
import com.centr.navigation.NavigationStore
import com.centr.navigation.ScreenMove
import com.centr.sound.Msadf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Sasdf(data: String?, context: Context, onClose: () -> Unit) {
    init {
        val hand = Msadf()
        data?.let { d ->
            when {
                hand.short == d.take(hand.short.length) -> {
                    NavigationStore.navigate(ScreenMove.Move)
                }

                d.length < 3 -> {
                    throw IllegalStateException()
                }

                hand.short != d.take(hand.short.length) -> {
                    onClose()
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            Save.safeSave(context, d)
                        } catch (_: Exception) {
                        }
                    }
                }

                else -> {}
            }
        }
    }
}