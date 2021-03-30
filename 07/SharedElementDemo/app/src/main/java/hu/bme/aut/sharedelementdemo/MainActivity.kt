package hu.bme.aut.sharedelementdemo

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnDetails.setOnClickListener {
            val intentDetails = Intent(this, DetailsActivity::class.java)

            val options = ActivityOptions
                .makeSceneTransitionAnimation(this, ivItemLogo, "sharedTransition")

            startActivity(intentDetails, options.toBundle())
        }
    }
}