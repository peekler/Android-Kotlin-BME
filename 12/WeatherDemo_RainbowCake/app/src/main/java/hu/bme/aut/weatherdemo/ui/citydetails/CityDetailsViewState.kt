package hu.bme.aut.weatherdemo.ui.citydetails

import hu.bme.aut.weatherdemo.ui.citydetails.models.UiWeatherData

sealed class CityDetailsViewState

object Initial : CityDetailsViewState()

object Loading : CityDetailsViewState()

data class CityDetailsReady(val weatherData: UiWeatherData) : CityDetailsViewState()

object Error : CityDetailsViewState()
