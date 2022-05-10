package hu.bme.simpleservicedemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnStop = findViewById<Button>(R.id.btnStop)

        val intentService = Intent(this, TimeService::class.java)

        btnStart.setOnClickListener {
            startService(intentService)
        }
        btnStop.setOnClickListener {
            stopService(intentService)
        }
    }
}