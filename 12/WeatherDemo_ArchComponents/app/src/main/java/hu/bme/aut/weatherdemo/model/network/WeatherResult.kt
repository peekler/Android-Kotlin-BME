package hu.bme.aut.weatherdemo.model.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResult(
    val coord: Coord?,
    val weather: List<Weather>?,
    val base: String?,
    val main: Main?,
    val visibility: Double?,
    val wind: Wind?,
    val clouds: Clouds?,
    val dt: Double?,
    val sys: Sys?,
    val timezone: Double?,
    val id: Double?,
    val name: String?,
    val cod: Double?
)

@JsonClass(generateAdapter = true)
data class Clouds(val all: Double?)

@JsonClass(generateAdapter = true)
data class Coord(val lon: Double?, val lat: Double?)

@JsonClass(generateAdapter = true)
data class Main(
    val temp: Double?,
    val feels_like: Double?,
    val temp_min: Double?,
    val temp_max: Double?,
    val pressure: Double?,
    val humidity: Double?
)

@JsonClass(generateAdapter = true)
data class Sys(
    val type: Double?,
    val id: Double?,
    val country: String?,
    val sunrise: Double?,
    val sunset: Double?
)

@JsonClass(generateAdapter = true)
data class Weather(
    val id: Double?,
    val main: String?,
    val description: String?,
    val icon: String?
)

@JsonClass(generateAdapter = true)
data class Wind(val speed: Double?, val deg: Double?)