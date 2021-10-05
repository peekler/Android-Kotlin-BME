package hu.bme.activityresultdemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.activityresultdemo.databinding.ActivityDetailsBinding
import java.util.*

class DetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(MainActivity.KEY_DATA)) {
            binding.tvData.text = intent.getStringExtra(MainActivity.KEY_DATA)
        }

        binding.btnYes.setOnClickListener {
            var intentResult = Intent()
            intentResult.putExtra(MainActivity.KEY_ANS, Date(System.currentTimeMillis()).toString())

            setResult(Activity.RESULT_OK, intentResult)
            finish()
        }
        binding.btnNo.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

    }
}