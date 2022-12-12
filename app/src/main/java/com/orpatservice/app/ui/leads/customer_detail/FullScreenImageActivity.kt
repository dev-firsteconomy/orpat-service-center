package com.orpatservice.app.ui.leads.customer_detail

import android.content.Intent
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ActivityFullScreenImageBinding
import com.orpatservice.app.utils.Constants
import kotlin.math.max
import kotlin.math.min


/**
 * Created by Vikas Singh on 01/01/22.
 */

class FullScreenImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullScreenImageBinding
    var scaleGestureDetector : ScaleGestureDetector?= null
    private var mScaleFactor = 1.0f
    private lateinit var mImageView: ImageView
    var matrix: Matrix? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullScreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivCancel.setOnClickListener {

            finish()
        }

        mImageView =binding.ivFullScreenImage

       /* Glide.with(this)
            .load(intent.getStringExtra(Constants.IMAGE_URL))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.color.gray)
            .into(binding.ivFullScreenImage)
*/      binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl((intent.getStringExtra(Constants.IMAGE_URL).toString()))
        binding.webView.settings.javaScriptEnabled = true


        // if you want to enable zoom feature
        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.builtInZoomControls = true
       // binding.webView.settings.displayZoomControls = true
        binding.webView.setBackgroundColor(Color.parseColor("#FFFFFF"))
       // binding.webView.webViewClient = WebViewLoadClient()
       // WebView.setWebContentsDebuggingEnabled(false)

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        scaleGestureDetector?.onTouchEvent(motionEvent)
        return true
    }

    // Zooming in and out in a bounded range
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = max(0.1f, min(mScaleFactor, 10.0f))
            mImageView.scaleX = mScaleFactor
            mImageView.scaleY = mScaleFactor
            return true
        }
    }

    private inner class WebViewLoadClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (Uri.parse(url).host == intent.getStringExtra(Constants.IMAGE_URL).toString()) {
                return false
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url)
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}