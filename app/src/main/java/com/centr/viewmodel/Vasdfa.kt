package com.centr.viewmodel

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import com.centr.ui.screens.Nasdf
import com.centr.viewmodel.model.Pvfas
import com.centr.pro.dp.core.bnasf

@SuppressLint("ViewConstructor")
class Vasdfa(
    private val activity: ComponentActivity,
    private val viewClient: Pvfas,
) : WebView(activity) {
    private val contentRoot: FrameLayout = FrameLayout(activity)
    val popupContainer: FrameLayout = FrameLayout(activity).apply {
        isVisible = false
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
    }
    val fullscreenContainer: FrameLayout = FrameLayout(activity).apply {
        isVisible = false
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
    }
    val content: ViewGroup = activity.findViewById(R.id.content)
    private var first = true
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (popupContainer.isNotEmpty()) {
                val top = popupContainer.getChildAt(popupContainer.childCount - 1) as WebView
                if (top.canGoBack()) {
                    top.goBack()
                } else {
                    top.stopLoading()
                    (top.parent as? ViewGroup)?.removeView(top)
                    top.destroy()
                    popupContainer.isVisible = popupContainer.isNotEmpty()
                }
                return
            }
            if (canGoBack()) {
                goBack()
            }
        }
    }


    private val chromeClient = Nasdf(activity, this, viewClient)

    init {
        Log.d("KKKKK", "CustomWebView init")

        content.addView(contentRoot)
        contentRoot.addView(
            this,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
        contentRoot.addView(popupContainer)
        contentRoot.addView(fullscreenContainer)
        contentRoot.isVisible = false
        activity.onBackPressedDispatcher.addCallback(activity, backPressedCallback)

        bnasf(this, viewClient, chromeClient)
    }

    fun showOneWebView() {
        if (first) {
            first = false
            for (i in content.childCount - 1 downTo 0) {
                val child = content.getChildAt(i)
                if (child != contentRoot) content.removeViewAt(i)
            }

            if (contentRoot.parent == null) {
                activity.requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                content.addView(contentRoot)
                newNotification(activity.activityResultRegistry)
            }
        }

        if (!contentRoot.isVisible)
            contentRoot.isVisible = true

        Log.d("KKKKK", "showWebView: ${contentRoot.isVisible}")
    }

    fun getW(): WebView {
        return this
    }
}

fun newNotification(registry: ActivityResultRegistry) {
    val launcher = registry.register(
        "requestPermissionKey",
        ActivityResultContracts.RequestPermission()
    ) { }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}
