package hu.bme.aut.bmehello

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnShow.setOnClickListener{
            var data = etName.text.toString() + Date(System.currentTimeMillis()).toString()

            tvData.text = data

            Toast.makeText(this, data, Toast.LENGTH_LONG).show()
        }
    }
}
