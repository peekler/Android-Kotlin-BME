package hu.bme.aut.databindingdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import hu.bme.aut.databindingdemo.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    data class User(var firstName: String, var lastName: String)

    companion object {
        private val FIRST_NAMES = listOf(
                "Dwight", "Jim", "Pam", "Michael", "Toby", "Andy", "Ryan",
                "Robert", "Jan", "Stanley", "Kevin", "Meredith", "Angela",
                "Oscar", "Phyllis", "Kelly", "Creed", "Darryl", "Holly"
        )
        private val LAST_NAMES = listOf(
                "Schrute", "Halpert", "Beesly", "Scott", "Flenderson", "Bernard", "Howard",
                "California", "Levinson", "Hudson", "Malone", "Palmer", "Martin",
                "Martinez", "Lapin", "Kapoor", "Bratton", "Philbin", "Flax"
        )
    }

    private val random = Random()
    private fun <T> List<T>.getRandomElement(): T = get(random.nextInt(size))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.user = User(FIRST_NAMES.getRandomElement(), LAST_NAMES.getRandomElement())
        binding.toastName = "Demo"



        btnShuffle.setOnClickListener {
            binding.user = User(FIRST_NAMES.getRandomElement(), LAST_NAMES.getRandomElement())

            //binding.toastName = "Demo23333"

            Toast.makeText(this, binding.cityName,  Toast.LENGTH_LONG).show()
        }
    }
}

@BindingAdapter(value = ["setToastName"])
fun TextView.bindToastName(userName: String?) {
    Toast.makeText(this.context, userName, Toast.LENGTH_LONG).show()
}