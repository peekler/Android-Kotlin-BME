package hu.bme.aut.weatherdemo.ui.cities

import hu.bme.aut.weatherdemo.ui.cities.models.UiCity

sealed class CitiesViewState

object Initial : CitiesViewState()

object Loading : CitiesViewState()

data class CitiesReady(val cities: List<UiCity>) : CitiesViewState()
