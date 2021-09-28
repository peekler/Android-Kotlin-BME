package hu.bme.aut.animationdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val demoAnim = AnimationUtils.loadAnimation(this, R.anim.demo_anim)
        val sendAnim = AnimationUtils.loadAnimation(this, R.anim.send_anim)

        val animListener = object: Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                Toast.makeText(this@MainActivity, "Message sent", Toast.LENGTH_LONG).show()

            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        }

        sendAnim.setAnimationListener(animListener)
        demoAnim.setAnimationListener(animListener)

        btnAnim.setOnClickListener {
            //btnAnim.startAnimation(demoAnim)
            tvMessage.startAnimation(sendAnim)
        }
    }
}