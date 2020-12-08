package hu.bme.aut.weatherdemo.ui.citydetails.models

data class UiWeatherData(
    val icon: String,
    val main: String,
    val description: String,
    val temperature: String,
    val sunrise: String,
    val sunset: String,
)
