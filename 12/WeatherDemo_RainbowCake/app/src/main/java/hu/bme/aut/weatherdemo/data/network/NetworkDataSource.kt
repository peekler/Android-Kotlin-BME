package hu.bme.aut.weatherdemo.data.network

import hu.bme.aut.weatherdemo.domain.models.DomainWeatherData
import hu.bme.aut.weatherdemo.util.network.NetworkResponse
import hu.bme.aut.weatherdemo.util.network.executeNetworkCall
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val weatherApi: WeatherApi
) {

    suspend fun getWeatherData(cityName: String): NetworkResponse<DomainWeatherData> =
        executeNetworkCall {
            weatherApi.getWeatherData(cityName).let {
                DomainWeatherData(
                    cityName = cityName,
                    icon = it.weather?.get(0)?.icon,
                    main = it.weather?.get(0)?.main,
                    description = it.weather?.get(0)?.description,
                    temperature = it.main?.temp?.toFloat(),
                    sunriseDate = getDateFromNumber(it.sys?.sunrise),
                    sunsetDate = getDateFromNumber(it.sys?.sunset)
                )
            }
        }

    private fun getDateFromNumber(number: Number?): Date? {
        val dateLong = number?.toLong()?.times(1000) ?: return null
        return Date(dateLong)
    }
}