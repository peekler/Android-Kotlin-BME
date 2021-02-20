package hu.bme.aut.hellobme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import hu.bme.aut.hellobme.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var myFriend = Person("Joe", "2000")
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main)
        binding.friend = myFriend

        binding.btnShowTime.setOnClickListener {
            //binding.tvData.setText(Date(System.currentTimeMillis()).toString())
            //binding.tvData.setText("Kor: ${friend.birth}")

            binding.friend = Person(myFriend.name,Date(System.currentTimeMillis()).toString())
        }
    }
}
