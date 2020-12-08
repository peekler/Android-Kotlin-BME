package hu.bme.aut.weatherdemo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import hu.bme.aut.weatherdemo.model.db.City

@Dao
interface CityDAO {
    @Query("SELECT * FROM city")
    fun getAllCities(): LiveData<List<City>>

    @Insert
    fun insertCity(city: City) : Long

    @Insert
    fun insertCities(vararg city: City): List<Long>

    @Insert
    fun insertCitiesList(cities: List<City>): List<Long>

    @Delete
    fun deleteCity(city: City)

    @Query("DELETE FROM city")
    fun deleteAllCity()

    @Update
    fun updateCity(city: City)
}