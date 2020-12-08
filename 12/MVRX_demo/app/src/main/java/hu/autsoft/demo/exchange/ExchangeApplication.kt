package hu.autsoft.demo.exchange

import android.app.Application
import com.airbnb.mvrx.Mavericks

class ExchangeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Mavericks.initialize(this)
    }
}