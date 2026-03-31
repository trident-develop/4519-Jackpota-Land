package com.centr.viewmodel.model

import android.content.Intent
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import com.centr.ui.components.Sasdf
import com.centr.viewmodel.Vasdfa
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Pvfas(
    private val activity: ComponentActivity,
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val uri = request?.url ?: return false
        if (uri.scheme == "about") return false
        val intent = when (uri.scheme) {
            "intent" -> runCatching {
                Intent.parseUri(uri.toString(), Intent.URI_INTENT_SCHEME)
            }.onFailure {
                //Log.e("WebView", "Bad intent uri: $uri", it)
            }.getOrNull()
            "mailto" -> Intent(Intent.ACTION_SENDTO, uri)
            "tel" -> Intent(Intent.ACTION_DIAL, uri)
            "http", "https", "blob", "data" -> null
            else -> Intent(Intent.ACTION_VIEW, uri)
        }
        intent?.let {
            try {
                activity.startActivity(it)
                return true
            } catch (_: Throwable) {
            }
            // Try to resolve package name from intent
            val packageName = intent.`package`
                ?: intent.component?.packageName
            if (packageName == null) {
                Toast.makeText(
                    activity,
                    "No application found!",
                    Toast.LENGTH_LONG
                ).show()
                return true
            }
            // Try to open Google Play via market:// intent
            try {
                val googlePlayIntent = Intent(
                    Intent.ACTION_VIEW,
                    "market://details?id=$packageName".toUri()
                )
                googlePlayIntent.setPackage("com.android.vending")
                activity.startActivity(googlePlayIntent)
                return true
            } catch (_: Throwable) {
            }
            // Try to open any market via market:// intent
            try {
                val marketIntent = Intent(
                    Intent.ACTION_VIEW,
                    "market://details?id=$packageName".toUri()
                )
                activity.startActivity(marketIntent)
                return true
            } catch (_: Throwable) {
            }
            // Last resort: try to open Google Play in browser
            try {
                val playStoreIntent = Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$packageName".toUri()
                )
                activity.startActivity(playStoreIntent)
            } catch (_: Throwable) {
                // All attempts to open Google Play failed
                Toast.makeText(activity, "No application found!", Toast.LENGTH_LONG).show()
            }
            return true
        }
        return false
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        CoroutineScope(Dispatchers.IO).launch {
            CookieManager.getInstance().flush()
        }

        Sasdf(url, activity) {
            (view as? Vasdfa)?.showOneWebView()
        }
    }
}
