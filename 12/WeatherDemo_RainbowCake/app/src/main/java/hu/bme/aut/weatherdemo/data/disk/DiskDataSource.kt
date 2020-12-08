package hu.bme.aut.weatherdemo.data.disk

import hu.bme.aut.weatherdemo.data.disk.dao.CityDao
import hu.bme.aut.weatherdemo.data.disk.models.RoomCity
import hu.bme.aut.weatherdemo.domain.models.DomainCity
import javax.inject.Inject

class DiskDataSource @Inject constructor(
    private val cityDao: CityDao
) {

    fun getAllCities(): List<DomainCity> {
        return cityDao.getAllCities().map(RoomCity::toDomainCity)
    }

    fun saveCity(cityName: String): DomainCity {
        val newCityId = cityDao.insertCity(RoomCity(null, cityName))
        return DomainCity(newCityId, cityName)
    }

    fun deleteCity(city: DomainCity) {
        cityDao.deleteCity(city.toRoomCity())
    }
}
