package hu.aut.android.kotlinwearnotifdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class FlameView(context: Context) : SurfaceView(context) {

    private val bmp: Bitmap
    private var loopThread: LoopThread? = null
    private val sprite: FlameSprite

    init {
        holder.addCallback(object : SurfaceHolder.Callback {

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                var retry = true
                loopThread!!.setRunning(false)
                while (retry) {
                    try {
                        loopThread!!.join()
                        retry = false
                    } catch (e: InterruptedException) {
                    }

                }
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                if (loopThread != null) {
                    loopThread!!.setRunning(false)
                }
                loopThread = LoopThread(this@FlameView)
                loopThread!!.setRunning(true)
                loopThread!!.start()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder, format: Int,
                width: Int, height: Int
            ) {
            }
        })
        bmp = BitmapFactory.decodeResource(resources, R.drawable.flame_sprite)
        sprite = FlameSprite(this, bmp)
    }

    public override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            canvas.drawColor(Color.BLACK)
            sprite.onDraw(canvas)
        }
    }
}