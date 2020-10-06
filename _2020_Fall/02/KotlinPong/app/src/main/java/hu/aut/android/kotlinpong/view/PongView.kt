package hu.aut.android.kotlinpong.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PongView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paintBg: Paint = Paint()
    private val paintLine: Paint = Paint()
    private val paintWhiteFill: Paint = Paint()
    private val paintText: Paint = Paint()

    private val PLAYER_WIDTH: Int = 180;
    private val PLAYER_HEIGHT: Int = 60;
    private var playerX: Float = 0f
    private var playerY: Float = 0f
    private var touchX: Float = 0f

    private var GAME_ENABLED = false
    private var circleX = 200f
    private var circleY = 200f
    private var CIRCLE_RAD = 50f
    private var dX = 10
    private var dY = 10
    private var point = 0

    init {
        paintBg.color = Color.BLACK
        paintBg.style = Paint.Style.FILL

        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 7f

        paintWhiteFill.color = Color.WHITE
        paintWhiteFill.style = Paint.Style.FILL

        paintText.color = Color.WHITE
        paintText.style = Paint.Style.STROKE
        paintText.textSize = 100f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        playerX = (width / 2 - PLAYER_WIDTH / 2).toFloat();

        paintText.textSize = height / 20f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBg)


        canvas?.drawRect(playerX,
                (height - PLAYER_HEIGHT).toFloat(),
                playerX + PLAYER_WIDTH,
                height.toFloat(), paintWhiteFill)

        canvas?.drawCircle(circleX, circleY, CIRCLE_RAD, paintWhiteFill)

        canvas?.drawText(point.toString(),20f, height/ 20 - 10f, paintText)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> touchX = event.x
            MotionEvent.ACTION_MOVE -> {
                handleMove(event)
            }
        }

        return true
    }

    private fun handleMove(event: MotionEvent) {
        playerX = playerX - (touchX - event.x)
        touchX = event.x
        playerX = if (playerX < 0) 0.toFloat()
        else if (playerX > width - PLAYER_WIDTH) {
            (width - PLAYER_WIDTH).toFloat()
        } else {
            playerX
        }
        invalidate()
    }

    fun startGame() {
        GAME_ENABLED = true
        GameThread().start()
    }

    fun stopGame() {
        GAME_ENABLED = false
        resetGame()
        invalidate()
    }

    private fun resetGame() {
        circleX = 200f
        circleY = 200f
        dX = 10
        dY = 10

        point = 0
    }

    inner class GameThread : Thread() {
        override fun run() {
            while (GAME_ENABLED) {
                circleX += dX
                circleY += dY


                if (circleX > width - CIRCLE_RAD) {
                    circleX = width - CIRCLE_RAD
                    dX *= -1
                } else if (circleX < CIRCLE_RAD) {
                    circleX = CIRCLE_RAD
                    dX *= -1
                }

                if (circleY >= height - CIRCLE_RAD - PLAYER_HEIGHT) {
                    if (circleX in playerX..playerX + PLAYER_WIDTH) {
                        circleY = height - CIRCLE_RAD - PLAYER_HEIGHT
                        dY *= -1
                        point++
                    } else {
                        resetGame()
                    }
                } else if (circleY < CIRCLE_RAD) {
                    circleY = CIRCLE_RAD
                    dY *= -1
                }

                postInvalidate()

                sleep(10)
            }
        }
    }


}