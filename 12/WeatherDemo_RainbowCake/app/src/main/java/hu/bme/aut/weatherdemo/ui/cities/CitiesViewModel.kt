package hu.bme.aut.weatherdemo.ui.cities

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.weatherdemo.ui.cities.models.UiCity
import javax.inject.Inject

class CitiesViewModel @Inject constructor(
    private val citiesPresenter: CitiesPresenter
) : RainbowCakeViewModel<CitiesViewState>(Initial) {

    fun load() = execute {
        viewState = Loading
        viewState = CitiesReady(citiesPresenter.getCities())
    }

    fun showDialog() = execute {
        postEvent(ShowAddCityDialog)
    }

    fun saveCity(cityName: String) = execute {
        viewState = Loading
        citiesPresenter.saveCity(cityName)
        viewState = CitiesReady(citiesPresenter.getCities())
    }

    fun deleteCity(city: UiCity) = execute {
        viewState = Loading
        citiesPresenter.deleteCity(city)
        viewState = CitiesReady(citiesPresenter.getCities())
    }

    object ShowAddCityDialog : OneShotEvent
}
