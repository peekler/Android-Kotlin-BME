package hu.bme.aut.firstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnDemo.setOnClickListener {
            var dateText = Date(System.currentTimeMillis()).toString()
            tvData.append("#$dateText")

            Toast.makeText(this, dateText, Toast.LENGTH_LONG).show()
        }
    }

}