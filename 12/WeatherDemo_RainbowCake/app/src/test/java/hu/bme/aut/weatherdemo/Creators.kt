package hu.bme.aut.weatherdemo

import hu.bme.aut.weatherdemo.domain.models.DomainCity
import hu.bme.aut.weatherdemo.ui.cities.models.UiCity

fun createDomainCity(
    cityId: Long = 0,
    cityName: String = "Test City"
) = DomainCity(cityId, cityName)

fun createUiCity(
    cityId: Long = 0,
    cityName: String = "Test City"
) = UiCity(cityId, cityName)