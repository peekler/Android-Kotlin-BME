package hu.aut.android.kotlinwearnotifdemo

import android.graphics.Canvas

class LoopThread(private val view: FlameView) : Thread() {
    private var running = false

    fun setRunning(run: Boolean) {
        running = run
    }

    override fun run() {
        val ticksPS = 1000 / FPS
        var startTime: Long
        var sleepTime: Long
        while (running) {
            var c: Canvas? = null
            startTime = System.currentTimeMillis()
            try {
                c = view.getHolder().lockCanvas()
                if (c != null) {
                    synchronized(view.getHolder()) {
                        view.onDraw(c)
                    }
                }
            } finally {
                if (c != null) {
                    view.holder.unlockCanvasAndPost(c)
                }
            }
            sleepTime = ticksPS - (System.currentTimeMillis() - startTime)
            try {
                if (sleepTime > 0)
                    Thread.sleep(sleepTime)
                else
                    Thread.sleep(10)
            } catch (e: Exception) {
            }

        }
    }

    companion object {
        internal val FPS: Long = 10
    }
}
