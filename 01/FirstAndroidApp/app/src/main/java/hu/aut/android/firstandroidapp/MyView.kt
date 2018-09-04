package hu.aut.android.firstandroidapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MyView(context: Context?, attrs: AttributeSet?) : View(context, attrs)

{

    var myPaint: Paint = Paint()

    init {
        myPaint.color = Color.BLACK
        myPaint.style = Paint.Style.FILL
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(
                0f, 0f,
                width.toFloat(), height.toFloat(), myPaint)
    }

}