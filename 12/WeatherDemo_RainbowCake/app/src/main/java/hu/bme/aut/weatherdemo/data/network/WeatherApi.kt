package hu.bme.aut.weatherdemo.data.network

import hu.bme.aut.weatherdemo.BuildConfig
import hu.bme.aut.weatherdemo.data.network.models.WeatherResult
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/data/2.5/weather")
    suspend fun getWeatherData(@Query("q") cityName: String,
                       @Query("units") units: String = "metric",
                       @Query("appid") appId: String = BuildConfig.APP_ID): WeatherResult
}