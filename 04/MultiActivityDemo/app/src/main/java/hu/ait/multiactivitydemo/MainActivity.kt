package hu.ait.multiactivitydemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val KEY_DATA = "KEY_DATA"
        const val REQ_ANSWER = 1001
        const val KEY_RES = "KEY_RES"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart.setOnClickListener {
            var intentDetails = Intent()

            intentDetails.setClass(this,
                DetailsActivity::class.java)

            intentDetails.putExtra(KEY_DATA,
                etData.text.toString())




            startActivityForResult(intentDetails, REQ_ANSWER)
        }
    }


    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_ANSWER){
            if (resultCode == Activity.RESULT_OK){
                var resp = data?.getStringExtra(KEY_RES)
                Toast.makeText(this, resp, Toast.LENGTH_LONG).show()
            } else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, getString(R.string.text_cancelled), Toast.LENGTH_LONG).show()
            }
        }

    }

}
