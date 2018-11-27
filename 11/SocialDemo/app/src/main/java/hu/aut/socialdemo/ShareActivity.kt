package hu.aut.socialdemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN

class ShareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "http://www.aut.bme.hu/")
        startActivity(Intent.createChooser(intent, "Share with"))
    }
}
