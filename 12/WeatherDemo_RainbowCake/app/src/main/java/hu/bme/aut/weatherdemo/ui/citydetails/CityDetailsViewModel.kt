package hu.bme.aut.weatherdemo.ui.citydetails

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class CityDetailsViewModel @Inject constructor(
    private val cityDetailsPresenter: CityDetailsPresenter
) : RainbowCakeViewModel<CityDetailsViewState>(Initial) {

    fun load(cityName: String) = execute {
        viewState = Loading

        val weatherData = cityDetailsPresenter.getWeatherData(cityName)

        viewState = if (weatherData != null) {
            CityDetailsReady(weatherData)
        } else {
            Error
        }
    }

}
