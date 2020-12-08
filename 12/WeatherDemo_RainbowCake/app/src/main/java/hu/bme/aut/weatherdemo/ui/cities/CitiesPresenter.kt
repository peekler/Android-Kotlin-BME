package hu.bme.aut.weatherdemo.ui.cities

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.weatherdemo.domain.interactors.CityInteractor
import hu.bme.aut.weatherdemo.domain.models.DomainCity
import hu.bme.aut.weatherdemo.ui.cities.models.UiCity
import javax.inject.Inject

class CitiesPresenter @Inject constructor(
    private val cityInteractor: CityInteractor
) {

    suspend fun getCities(): List<UiCity> = withIOContext {
        val cities = cityInteractor.getSavedCities()
        cities.map { it.toUiCity() }
    }

    suspend fun saveCity(cityName: String): UiCity = withIOContext {
        val newCity = cityInteractor.saveCity(cityName)
        newCity.toUiCity()
    }

    suspend fun deleteCity(city: UiCity) = withIOContext {
        cityInteractor.deleteCity(city.toDomainCity())
    }
}

private fun DomainCity.toUiCity(): UiCity {
    return UiCity(
        cityId = cityId,
        cityName = cityName.trim()
    )
}

private fun UiCity.toDomainCity(): DomainCity {
    return DomainCity(
        cityId = cityId,
        cityName = cityName
    )
}
