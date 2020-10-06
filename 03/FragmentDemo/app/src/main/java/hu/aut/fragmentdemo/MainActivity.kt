package hu.aut.fragmentdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout

class MainActivity : AppCompatActivity() {

    var isLandscape=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState==null) {
            showHomeFragment()
        }

        val detailFragmentContainer: FrameLayout? =findViewById(R.id.detailFragmentContainer)
        isLandscape=detailFragmentContainer!=null

    }

    private fun showHomeFragment() {
        val homeFragment=HomeFragment()

        val ft=supportFragmentManager.beginTransaction()
        ft.add(R.id.fragmentContainer,homeFragment,HomeFragment.TAG)
        ft.commit()

    }

    fun showDetails(name: String) {
        val detailsFragment=DetailFragment.newInstance(name)

        val ft=supportFragmentManager.beginTransaction()

        if (isLandscape) {
            ft.add(R.id.detailFragmentContainer,detailsFragment,DetailFragment.TAG)
        }else{
            ft.add(R.id.fragmentContainer,detailsFragment,DetailFragment.TAG)
        }


        ft.addToBackStack(DetailFragment.TAG)
        ft.commit()
    }
}
