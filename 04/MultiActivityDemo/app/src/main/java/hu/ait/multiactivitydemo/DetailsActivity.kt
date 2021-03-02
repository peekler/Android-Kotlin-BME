package hu.ait.multiactivitydemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        var data = intent.getStringExtra(
            MainActivity.KEY_DATA)

        tvData.text = data

        btnAccept.setOnClickListener {
            var intentResult = Intent()
            intentResult.putExtra(
                MainActivity.KEY_RES, "accept")

            setResult(Activity.RESULT_OK, intentResult)

            finish()
        }

        btnDecline.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        Toast.makeText(this,
            "You can not exit",
            Toast.LENGTH_LONG).show()
    }
}
