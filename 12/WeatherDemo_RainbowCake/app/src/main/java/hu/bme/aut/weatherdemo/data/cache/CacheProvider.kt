package hu.bme.aut.weatherdemo.data.cache

import android.content.Context
import hu.autsoft.krate.SimpleKrate
import hu.autsoft.krate.gson.gsonPref
import hu.bme.aut.weatherdemo.domain.models.DomainWeatherData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheProvider @Inject constructor(context: Context) : SimpleKrate(context) {

    var lastCity: DomainWeatherData? by gsonPref("last_city")
}