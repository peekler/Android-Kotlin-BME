package hu.bme.aut.weatherdemo.ui.citydetails

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.weatherdemo.domain.interactors.WeatherInteractor
import hu.bme.aut.weatherdemo.domain.models.DomainWeatherData
import hu.bme.aut.weatherdemo.ui.citydetails.models.UiWeatherData
import hu.bme.aut.weatherdemo.util.network.SomeResult
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CityDetailsPresenter @Inject constructor(
    private val weatherInteractor: WeatherInteractor
) {

    suspend fun getWeatherData(cityName: String): UiWeatherData? = withIOContext {
        when (val response = weatherInteractor.getWeatherData(cityName)) {
            is SomeResult -> response.result.toUiWeatherData()
            else -> null
        }
    }
}

private fun DomainWeatherData.toUiWeatherData(): UiWeatherData {
    val sdf = SimpleDateFormat("h:mm a z", Locale.getDefault())
    return UiWeatherData(
        icon = icon ?: "",
        main = main ?: "N/A",
        description = description ?: "N/A",
        temperature = temperature?.toString() ?: "N/A",
        sunrise = sunriseDate.toDateString(sdf),
        sunset = sunsetDate.toDateString(sdf)
    )
}

private fun Date?.toDateString(simpleDateFormat: SimpleDateFormat): String {
    return if (this != null) {
        simpleDateFormat.format(this)
    } else {
        "N/A"
    }
}
