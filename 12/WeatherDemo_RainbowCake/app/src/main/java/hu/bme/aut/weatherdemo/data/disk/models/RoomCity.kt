package hu.bme.aut.weatherdemo.data.disk.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class RoomCity (
    @PrimaryKey(autoGenerate = true) var cityId : Long?,
    @ColumnInfo(name = "cityname") var cityName : String
)