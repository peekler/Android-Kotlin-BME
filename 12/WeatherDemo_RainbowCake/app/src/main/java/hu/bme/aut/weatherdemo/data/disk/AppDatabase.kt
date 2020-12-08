package hu.bme.aut.weatherdemo.data.disk

import androidx.room.Database
import androidx.room.RoomDatabase
import hu.bme.aut.weatherdemo.data.disk.dao.CityDao
import hu.bme.aut.weatherdemo.data.disk.models.RoomCity


@Database(
    entities = [RoomCity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao
}