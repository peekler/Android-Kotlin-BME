package hu.bme.aut.bottomnavfragmentsdemo

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.app.Fragment
import hu.bme.aut.bottomnavfragmentsdemo.fragments.FragmentThree
import hu.bme.aut.bottomnavfragmentsdemo.fragments.FragmentTwo
import hu.bme.aut.bottomnavfragmentsdemo.fragments.FragmentOne



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(myOnNavigationItemSelectedListener)

        showFragmentByTag(FragmentOne.TAG, false)
    }

    public fun showFragmentByTag(tag: String,
                                  toBackStack: Boolean) {
        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            if (FragmentOne.TAG == tag) {
                fragment = FragmentOne()
            } else if (FragmentTwo.TAG == tag) {
                fragment = FragmentTwo()
            } else if (FragmentThree.TAG == tag) {
                fragment = FragmentThree()
            }
        }

        if (fragment != null) {
            val ft = supportFragmentManager
                .beginTransaction()
            ft.replace(R.id.fragmentContainer, fragment!!, tag)
            if (toBackStack) {
                ft.addToBackStack(null)
            }
            ft.commit()
        }
    }

    private val myOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                showFragmentByTag(FragmentOne.TAG, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                showFragmentByTag(FragmentTwo.TAG, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                showFragmentByTag(FragmentThree.TAG, true)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
