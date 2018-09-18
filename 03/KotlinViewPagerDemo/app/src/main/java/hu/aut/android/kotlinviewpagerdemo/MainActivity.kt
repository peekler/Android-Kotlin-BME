package hu.aut.android.kotlinviewpagerdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import hu.aut.android.kotlinviewpagerdemo.adapter.DemoPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager.adapter = DemoPagerAdapter(supportFragmentManager);
    }
}
