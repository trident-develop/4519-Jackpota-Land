package com.centr.data

import android.app.Activity
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.centr.pro.dp.core.IpScore
import com.centr.pro.dp.network.bsdfg
import kotlinx.coroutines.flow.first

private val Context.dataPref by preferencesDataStore(name = "kk")
object Save {
    private val KEY_STRING = stringPreferencesKey("rrr")

    suspend fun safeSave(context: Context, value: String) {
        val dataStore = context.dataPref

        val existing = dataStore.data.first()[KEY_STRING]
        if (existing != null) {
            throw IllegalStateException("Value already saved: $existing")
        }

        IpScore.invoke(context as Activity)
        bsdfg()
        dataStore.edit { prefs ->
            prefs[KEY_STRING] = value
        }
    }

    suspend fun getUrl(context: Context): String {
        val dataStore = context.dataPref
        return dataStore.data.first()[KEY_STRING] ?: ""
    }
}
