package hu.bme.aut.weatherdemo.data.disk

import hu.bme.aut.weatherdemo.data.disk.models.RoomCity
import hu.bme.aut.weatherdemo.domain.models.DomainCity

fun RoomCity.toDomainCity(): DomainCity {
    return DomainCity(
        cityId = cityId ?: 0,
        cityName = cityName
    )
}

fun DomainCity.toRoomCity(): RoomCity {
    return RoomCity(
        cityId = cityId,
        cityName = cityName
    )
}
