package hu.aut.android.kotlinwearnotifdemo

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect

class FlameSprite(private val flameView: FlameView, private val bmp: Bitmap) {
    private val x = 0
    private val y = 0
    private var currentFrame = 0
    private val width: Int
    private val height: Int

    init {
        this.width = bmp.width / BMP_COLUMNS
        this.height = bmp.height / BMP_ROWS
    }

    private fun update() {
        currentFrame = ++currentFrame
    }

    fun onDraw(canvas: Canvas) {
        update()
        val srcX = currentFrame % BMP_COLUMNS * width
        val srcY = currentFrame / BMP_COLUMNS % BMP_ROWS * height
        val src = Rect(srcX, srcY, srcX + width, srcY + height)
        val dst = Rect(x, y, flameView.width, flameView.height)
        canvas.drawBitmap(bmp, src, dst, null)
    }

    companion object {
        private val BMP_ROWS = 4
        private val BMP_COLUMNS = 4
    }
}