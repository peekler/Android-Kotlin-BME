package hu.autsoft.demo.exchange.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.autsoft.demo.exchange.R
import hu.autsoft.demo.exchange.ui.exchange.ExchangeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, ExchangeFragment())
                .commitAllowingStateLoss()
        }
    }
}