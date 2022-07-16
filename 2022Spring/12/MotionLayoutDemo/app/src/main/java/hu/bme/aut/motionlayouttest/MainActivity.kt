package hu.bme.aut.motionlayouttest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        keyPositionButton.setOnClickListener {
            val intent = Intent(this, KeyPositionDemoActivity::class.java)
            startActivity(intent)
        }

        toolbarButton.setOnClickListener {
            val intent = Intent(this, ToolbarDemoActivity::class.java)
            startActivity(intent)
        }
    }
}