package hu.bme.aut.weatherdemo.repository

import androidx.lifecycle.LiveData
import hu.bme.aut.weatherdemo.database.CityDAO
import hu.bme.aut.weatherdemo.model.db.City

class CityRepository(private val cityDao: CityDAO) {

    val allCities: LiveData<List<City>> = cityDao.getAllCities()

    suspend fun insert(city: City) {
        cityDao.insertCity(city)
    }

    suspend fun delete(city: City) {
        cityDao.deleteCity(city)
    }
}