package hu.aut.android.draganddropgamedemo

import android.content.ClipData
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private inner class MyTouchListener : View.OnTouchListener {
        override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                val shadowBuilder = View.DragShadowBuilder(
                    view
                )

                view.startDragAndDrop(ClipData.newPlainText("", ""),
                    shadowBuilder, view, 0)
                draggedView = view

                return true
            }

            return false
        }

    }

    private var draggedView: View? = null
    private val myTouchListener = MyTouchListener()

    private val colorTexts = arrayOf("Blue", "Green", "Yellow", "Red")
    private val colorValues = arrayOf(0x007FFF, 0x00ff00, 0xffea00, 0xff1100)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        color1.setOnTouchListener(myTouchListener)
        color1.tag = "Blue"
        color2.setOnTouchListener(myTouchListener)
        color2.tag = "Green"
        color3.setOnTouchListener(myTouchListener)
        color3.tag = "Yellow"
        color4.setOnTouchListener(myTouchListener)
        color4.tag = "Red"

        basket.setOnDragListener(MyDragListener())

        basket.setOnClickListener{
            showGeneratedColor()
        }
    }

    val rand: Random = Random(System.currentTimeMillis())
    var genTimeStamp: Long = 0
    var currentGenColor: String = "Green"

    private fun showGeneratedColor() {
        var gen = rand.nextInt(4)
        currentGenColor = colorTexts[gen]
        Toast.makeText(this, currentGenColor, Toast.LENGTH_LONG).show()
        genTimeStamp = System.currentTimeMillis()
    }

    private inner class MyDragListener : View.OnDragListener {
        override fun onDrag(v: View, event: DragEvent): Boolean {
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                }
                DragEvent.ACTION_DRAG_ENTERED -> Toast.makeText(this@MainActivity, "Entered", Toast.LENGTH_SHORT)
                        .show()
                DragEvent.ACTION_DRAG_EXITED -> Toast.makeText(this@MainActivity, "Exited", Toast.LENGTH_SHORT)
                        .show()
                DragEvent.ACTION_DROP -> {
                    //Toast.makeText(this@MainActivity, "Drop", Toast.LENGTH_SHORT)
                    //        .show()
                    if (draggedView != null) {
                        basket.background = draggedView?.background

                        if (TextUtils.equals(draggedView?.tag as String, currentGenColor)) {
                            val winTime = System.currentTimeMillis() - genTimeStamp
                            val sec = winTime / 1000.0f;
                            val secFormat = String.format("%.3f", sec)

                            Toast.makeText(this@MainActivity, "Win in $secFormat ms", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            return true
        }
    }

}
