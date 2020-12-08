package hu.bme.aut.weatherdemo.domain.interactors

import hu.bme.aut.weatherdemo.data.disk.DiskDataSource
import hu.bme.aut.weatherdemo.domain.models.DomainCity
import javax.inject.Inject

class CityInteractor @Inject constructor(
    private val diskDataSource: DiskDataSource
) {

    fun getSavedCities(): List<DomainCity> {
        return diskDataSource.getAllCities()
    }

    fun saveCity(cityName: String): DomainCity {
        return diskDataSource.saveCity(cityName)
    }

    fun deleteCity(city: DomainCity) {
        diskDataSource.deleteCity(city)
    }
}