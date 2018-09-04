package hu.aut.android.firstandroidapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Date

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTime.setOnClickListener{
            Toast.makeText(this@MainActivity,
                    Date(System.currentTimeMillis()).toString(),
                    Toast.LENGTH_LONG
                    ).show()
        }
    }
}
