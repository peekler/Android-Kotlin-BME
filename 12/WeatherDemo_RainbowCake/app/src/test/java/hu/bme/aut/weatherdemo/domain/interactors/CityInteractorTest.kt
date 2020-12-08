package hu.bme.aut.weatherdemo.domain.interactors

import com.google.common.truth.Truth.assertThat
import hu.bme.aut.weatherdemo.createDomainCity
import hu.bme.aut.weatherdemo.data.disk.DiskDataSource
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CityInteractorTest : BehaviorSpec({

    val diskDataSource: DiskDataSource = mockk()

    val cityInteractor = CityInteractor(diskDataSource)

    Given("a single city with id=1 saved to the database") {
        every { diskDataSource.getAllCities() } returns listOf(createDomainCity(cityId = 1))

        When("getSavedCities is called") {
            val result = cityInteractor.getSavedCities()

            Then("city with id=1 should be returned") {
                assertThat(result).containsExactly(createDomainCity(cityId = 1))
            }
        }
    }

    Given("disk data source can save cities") {
        every { diskDataSource.saveCity(any()) } returns createDomainCity()

        When("saveCity with name Test is called") {
            cityInteractor.saveCity("Test")

            Then("DiskDataSource should save a city with name Test") {
                verify { diskDataSource.saveCity("Test") }
            }
        }
    }

})
