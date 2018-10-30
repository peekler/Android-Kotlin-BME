package hu.aut.android.mediaplaydemo

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MediaPlayer.OnPreparedListener {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnRingtone.setOnClickListener {
            playNotifTone()
        }

        btnStart.setOnClickListener {
            /*mediaPlayer = MediaPlayer.create(this@MainActivity,
                Uri.parse("http://babcomaut.aut.bme.hu/tmp/demo.mp3")
                )*/
            mediaPlayer = MediaPlayer.create(this@MainActivity,
                R.raw.demo)

            mediaPlayer.setOnPreparedListener(this@MainActivity)
        }

        btnStop.setOnClickListener {
            //mediaPlayer?.stop()
            mediaPlayer?.seekTo(61000)
        }
    }

    private fun playNotifTone() {
        val uriNotif = RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_NOTIFICATION
        )
        val r = RingtoneManager.getRingtone(
            applicationContext, uriNotif
        )
        r.play()
    }

    override fun onPrepared(player: MediaPlayer) {
        mediaPlayer.start()
    }

    override fun onStop() {
        mediaPlayer?.stop()
        super.onStop()
    }
}
