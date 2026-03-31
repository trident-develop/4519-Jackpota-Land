package com.centr.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import com.centr.pro.dp.core.bnasf
import com.centr.viewmodel.Vasdfa
import com.centr.viewmodel.model.Pvfas

class Nasdf(
    private val activity: ComponentActivity,
    private val ownerWebView: Vasdfa,
    private val viewClient: Pvfas,
) : WebChromeClient() {
    private var valueCallback: ValueCallback<Array<Uri>>? = null
    private var pendingPermissionRequest: PermissionRequest? = null
    private val input = "image/*"
    private var customView: View? = null
    private var customViewCallback: CustomViewCallback? = null
    private val launcher: ActivityResultLauncher<String> =
        activity.registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            valueCallback?.onReceiveValue(it.toTypedArray())
        }
    private val cameraPermLauncher: ActivityResultLauncher<String> =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            val req = pendingPermissionRequest ?: return@registerForActivityResult
            pendingPermissionRequest = null
            if (granted) {
                req.grant(req.resources)
            } else {
                req.deny()
            }
        }

    override fun onPermissionRequest(request: PermissionRequest?) {
        if (request == null) return
        val wantsCamera =
            request.resources?.contains(PermissionRequest.RESOURCE_VIDEO_CAPTURE) == true
        if (!wantsCamera) {
            request.deny()
            return
        }
        val granted = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) {
            request.grant(request.resources)
            return
        }
        pendingPermissionRequest?.deny()
        pendingPermissionRequest = request
        cameraPermLauncher.launch(Manifest.permission.CAMERA)
    }

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?,
    ): Boolean {
        valueCallback = filePathCallback
        launcher.launch(input)
        return true
    }

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        if (view == null) return
        if (customView != null) {
            callback?.onCustomViewHidden()
            return
        }
        customView = view
        customViewCallback = callback
        ownerWebView.isVisible = false
        ownerWebView.popupContainer.isVisible = false
        ownerWebView.fullscreenContainer.apply {
            removeAllViews()
            addView(
                view,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
            isVisible = true
        }
    }

    override fun onHideCustomView() {
        val view = customView ?: return
        ownerWebView.fullscreenContainer.apply {
            removeView(view)
            isVisible = false
        }
        ownerWebView.isVisible = true
        ownerWebView.popupContainer.isVisible = ownerWebView.popupContainer.isNotEmpty()
        customViewCallback?.onCustomViewHidden()
        customViewCallback = null
        customView = null
    }

    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?,
    ): Boolean {
        if (resultMsg == null) return false
        val popup = WebView(activity)
        bnasf(popup, viewClient, this)
        ownerWebView.popupContainer.isVisible = true
        ownerWebView.popupContainer.addView(
            popup,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
        val transport = resultMsg.obj as WebView.WebViewTransport
        transport.webView = popup
        resultMsg.sendToTarget()
        return true
    }

    override fun onCloseWindow(window: WebView?) {
        val popup = window ?: return
        (popup.parent as? ViewGroup)?.removeView(popup)
        ownerWebView.popupContainer.isVisible = ownerWebView.popupContainer.isNotEmpty()
        popup.stopLoading()
        popup.destroy()
    }
}