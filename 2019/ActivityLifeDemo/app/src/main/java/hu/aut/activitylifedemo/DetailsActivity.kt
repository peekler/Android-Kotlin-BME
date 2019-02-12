package hu.aut.activitylifedemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
    }

    var tryTime = 1

    override fun onBackPressed() {
        //super.onBackPressed()

        Toast.makeText(this,
            getString(R.string.text_try,tryTime),
            Toast.LENGTH_LONG).show()
        tryTime++
    }
}
