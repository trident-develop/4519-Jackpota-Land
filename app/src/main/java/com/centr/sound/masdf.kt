package com.centr.sound

import androidx.activity.ComponentActivity
import com.centr.data.Save.getUrl
import com.centr.viewmodel.Vasddf
import com.centr.ui.screens.vbasdf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun masdf(activity: ComponentActivity, factory: Vasddf) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val url = getUrl(activity)
            if (url.isBlank()) {
                //            Log.d("KKKKK", "NO LINK")
                factory.getR()
                vbasdf(activity, factory)
            } else {
                withContext(Dispatchers.Main) {
                    val w = factory.getOneV().getW()
                    w.requestFocus()
                    w.loadUrl(url)
                }
            }

        } catch (e: Exception) {
        }
    }
}
