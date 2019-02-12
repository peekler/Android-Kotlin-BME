package hu.aut.activitylifedemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("TAG_LIFE", "onCreate called")

        btnDetails.setOnClickListener {

            var intentDetails = Intent()
            intentDetails.setClass(this,
                DetailsActivity::class.java)
            startActivity(intentDetails)
        }
    }



    override fun onStart() {
        super.onStart()

        Log.d("TAG_LIFE", "onStart called")
    }

    override fun onResume() {
        super.onResume()

        Log.d("TAG_LIFE", "onResume called")
    }

    override fun onPause() {
        super.onPause()

        Log.d("TAG_LIFE", "onPause called")
    }

    override fun onStop() {
        super.onStop()

        Log.d("TAG_LIFE", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("TAG_LIFE", "onDestroy called")
    }

}
