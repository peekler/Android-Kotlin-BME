package hu.bme.aut.weatherdemo.data.disk.dao

import androidx.room.*
import hu.bme.aut.weatherdemo.data.disk.models.RoomCity

@Dao
interface CityDao {
    @Query("SELECT * FROM cities")
    fun getAllCities(): List<RoomCity>

    @Insert
    fun insertCity(city: RoomCity) : Long

    @Delete
    fun deleteCity(city: RoomCity)

    @Update
    fun updateCity(city: RoomCity)
}