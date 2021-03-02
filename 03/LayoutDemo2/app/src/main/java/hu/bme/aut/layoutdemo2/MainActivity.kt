package hu.bme.aut.layoutdemo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hu.bme.aut.layoutdemo2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.includeLayout.btnDemo2.setOnClickListener {
            Toast.makeText(this, "mukodik", Toast.LENGTH_LONG).show()
        }
    }
}