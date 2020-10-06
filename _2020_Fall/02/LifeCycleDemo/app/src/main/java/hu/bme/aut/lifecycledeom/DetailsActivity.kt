package hu.bme.aut.lifecycledeom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        var data = intent.extras?.getString("KEY_DATA")
        button.text = data

        Toast.makeText(this, getString(R.string.game_status, "John", 3), Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, getString(R.string.never_toast), Toast.LENGTH_LONG).show()
    }
}