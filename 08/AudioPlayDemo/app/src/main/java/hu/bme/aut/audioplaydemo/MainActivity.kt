package hu.bme.aut.audioplaydemo

import android.media.MediaPlayer
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.audioplaydemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MediaPlayer.OnPreparedListener {

    lateinit var binding: ActivityMainBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRingtone.setOnClickListener {
            playNotifTone()
        }

        binding.btnStart.setOnClickListener {
            mediaPlayer = MediaPlayer.create(this@MainActivity,
                    R.raw.demo)

            mediaPlayer.setOnPreparedListener(this@MainActivity)
        }

        binding.btnStop.setOnClickListener {
            mediaPlayer?.stop()
            //mediaPlayer?.seekTo(61000)
        }
    }

    private fun playNotifTone() {
        val uriNotif = RingtoneManager.getDefaultUri(
                RingtoneManager.TYPE_NOTIFICATION
        )
        val notiftone = RingtoneManager.getRingtone(
                applicationContext, uriNotif
        )
        notiftone.play()
    }

    override fun onPrepared(mp: MediaPlayer) {
        mediaPlayer.start()
    }

    override fun onStop() {
        mediaPlayer.stop()
        super.onStop()
    }
}