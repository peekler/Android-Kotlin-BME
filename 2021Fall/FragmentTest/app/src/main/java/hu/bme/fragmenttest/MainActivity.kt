package hu.bme.fragmenttest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.fragmenttest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showHomeFragment()
    }

    private fun showHomeFragment() {
        val homeFragment=HomeFragment.newInstance()

        val ft=supportFragmentManager.beginTransaction()
        ft.add(R.id.fragmentContainer,homeFragment,"HomeFragment")
        ft.commit()
    }

    fun showDetails() {
        val detailsFragment=DetailsFragment.newInstance()

        val ft=supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentContainer,detailsFragment,"DetailFragment")

        ft.addToBackStack(null)
        ft.commit()
    }
}