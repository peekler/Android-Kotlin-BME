package hu.aut.android.webviewdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Array.getLength




class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBarWebLoad.max = 100

        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                loadSite(request.url.toString())
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressBarWebLoad.progress = 100
                progressBarWebLoad.visibility = View.GONE
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressBarWebLoad.progress = newProgress
            }
        }

        webView.settings.builtInZoomControls = true

        loadSite("https://www.aut.bme.hu")
    }

    private fun loadSite(url: String) {
        progressBarWebLoad.visibility = View.VISIBLE
        progressBarWebLoad.progress = 0
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Billentyű esemény kezelése...
        }
        return super.onKeyDown(keyCode, event)
    }
}
