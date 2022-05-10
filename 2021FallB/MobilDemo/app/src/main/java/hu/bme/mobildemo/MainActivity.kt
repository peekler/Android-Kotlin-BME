package hu.bme.mobildemo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutMain = findViewById<ConstraintLayout>(R.id.layoutMain)
        val btnColor = findViewById<Button>(R.id.btnBackground)

        val animCircle = AnimationUtils.loadAnimation(this,
            R.anim.circleanim)

        btnColor.setOnClickListener {
            layoutMain.setBackgroundColor(Color.GREEN)
            btnColor.startAnimation(animCircle)
        }
    }
}