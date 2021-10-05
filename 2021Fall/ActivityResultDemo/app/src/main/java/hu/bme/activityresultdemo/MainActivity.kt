package hu.bme.activityresultdemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import hu.bme.activityresultdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val KEY_DATA = "KEY_DATA"
        const val KEY_ANS = "KEY_ANS"
    }


    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            var intentDetails = Intent(this,
                DetailsActivity::class.java)
            intentDetails.putExtra(KEY_DATA,
                binding.etData.text.toString())


            // start details and expect result...
            startForResult.launch(intentDetails)
        }
    }


    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            var data = "Result: ${intent?.getStringExtra(KEY_ANS)}"
            Toast.makeText(this, data, Toast.LENGTH_LONG).show()
            binding.tvResult.text = data

        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "CANCELLED", Toast.LENGTH_LONG).show()
        }
    }


}