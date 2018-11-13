package hu.aut.android.servicedemo.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.widget.TextView
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.content.Context.WINDOW_SERVICE
import android.os.Handler
import android.os.IBinder
import android.view.*
import java.util.Date
import android.view.WindowManager
import android.os.Build
import hu.aut.android.servicedemo.R


class FloatingService : Service() {

    private var enabled = true
    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private var tvTime: TextView? = null

    private inner class MyTimeShower : Thread() {
        override fun run() {
            val h = Handler(this@FloatingService.getMainLooper())
            while (enabled) {
                h.post { tvTime?.text = Date(System.currentTimeMillis()).toString() }


                try {
                    sleep(5000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
    }

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        floatingView = (getSystemService(
                LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.float_layout, null)

        tvTime = floatingView?.findViewById<TextView>(R.id.tvTime)
        tvTime?.setText(Date(System.currentTimeMillis()).toString())

        val LAYOUT_FLAG: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)

        params.gravity = Gravity.TOP or Gravity.LEFT
        params.x = 0
        params.y = 100

        windowManager?.addView(floatingView, params)

        floatingView?.setOnTouchListener(object : View.OnTouchListener {
            private var initialX: Int = 0
            private var initialY: Int = 0
            private var initialTouchX: Float = 0.toFloat()
            private var initialTouchY: Float = 0.toFloat()

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                    }
                    MotionEvent.ACTION_UP -> {
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = initialX + (event.rawX - initialTouchX).toInt()
                        params.y = initialY + (event.rawY - initialTouchY).toInt()
                        windowManager!!.updateViewLayout(floatingView, params)
                    }
                }
                return false
            }
        })

    }

    override fun onDestroy() {
        enabled = false
        if (floatingView != null) windowManager?.removeView(floatingView)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        enabled = true
        MyTimeShower().start()
        return super.onStartCommand(intent, flags, startId)
    }
}
