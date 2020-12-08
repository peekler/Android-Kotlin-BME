package hu.bme.aut.weatherdemo.domain.interactors

import hu.bme.aut.weatherdemo.data.cache.CacheProvider
import hu.bme.aut.weatherdemo.domain.models.DomainWeatherData
import hu.bme.aut.weatherdemo.data.network.NetworkDataSource
import hu.bme.aut.weatherdemo.util.network.*
import javax.inject.Inject

class WeatherInteractor @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val cacheProvider: CacheProvider
) {

    suspend fun getWeatherData(cityName: String): DataTransferResponse<DomainWeatherData> {
        return when (val response = networkDataSource.getWeatherData(cityName)) {
            is NetworkResult -> {
                cacheProvider.lastCity = response.result
                DataTransferSuccess(response.result)
            }
            else -> {
                val lastCity = cacheProvider.lastCity
                if (lastCity != null && lastCity.cityName == cityName) {
                    NetworkUnavailableCached(lastCity)
                } else {
                    NetworkUnavailableNotCached
                }
            }
        }
    }
}