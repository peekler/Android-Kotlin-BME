package hu.bme.aut.bmehelloandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.bmehelloandroid.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setContentView(R.layout.activity_main)

        //val btnShow = findViewById<Button>(R.id.btnShow)
        //val tvData = findViewById<TextView>(R.id.tvData)

        binding.btnShow.setOnClickListener {
            binding.tvData.text = Date(System.currentTimeMillis()).toString()
        }
    }

}