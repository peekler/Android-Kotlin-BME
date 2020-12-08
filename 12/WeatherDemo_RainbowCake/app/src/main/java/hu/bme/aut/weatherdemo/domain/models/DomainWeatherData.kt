package hu.bme.aut.weatherdemo.domain.models

import java.util.*

data class DomainWeatherData(
    val cityName: String,
    val icon: String?,
    val main: String?,
    val description: String?,
    val temperature: Float?,
    val sunriseDate: Date?,
    val sunsetDate: Date?,
)