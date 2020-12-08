package hu.bme.aut.viewbindingdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.viewbindingdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        binding.tvHello.text="DEMO"
    }
}

