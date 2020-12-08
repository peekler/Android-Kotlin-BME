package hu.bme.aut.weatherdemo.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class City (
    @PrimaryKey(autoGenerate = true) var cityId: Long?,
    @ColumnInfo(name = "cityname") var name : String
)