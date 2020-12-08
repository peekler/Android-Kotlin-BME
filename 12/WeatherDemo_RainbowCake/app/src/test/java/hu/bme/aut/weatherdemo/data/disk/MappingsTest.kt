package hu.bme.aut.weatherdemo.data.disk

import hu.bme.aut.weatherdemo.data.disk.models.RoomCity
import hu.bme.aut.weatherdemo.domain.models.DomainCity
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MappingsTest : StringSpec({

    "test RoomCity to DomainCity" {
        val roomCity = RoomCity(1, "Test")

        val domainCity = roomCity.toDomainCity()

        domainCity shouldBe DomainCity(1, "Test")
    }

    "test DomainCity to RoomCity" {
        val domainCity = DomainCity(1, "Test")

        val roomCity = domainCity.toRoomCity()

        roomCity shouldBe RoomCity(1, "Test")
    }
})
