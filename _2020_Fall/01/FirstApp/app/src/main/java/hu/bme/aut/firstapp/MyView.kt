package hu.bme.aut.firstapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MyView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    lateinit var paintCircle: Paint

    init {
        paintCircle = Paint()
        paintCircle.color = Color.GREEN
        paintCircle.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(100f, 100f, 70f, paintCircle)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)

        //event.x

        invalidate() // újra rajolja a view
    }

}