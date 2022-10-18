package hu.webuni.animationdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import hu.webuni.animationdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val anim = AnimationUtils.loadAnimation(this,
            R.anim.button_anim)

        anim.setAnimationListener(
            object:Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    Toast.makeText(this@MainActivity,
                        "Animation OVER", Toast.LENGTH_SHORT).show()
                    // Intent... launc scrolling activity
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }
            }
        )


        binding.btnPlay.setOnClickListener {
            binding.btnPlay.startAnimation(anim)
        }

    }
}