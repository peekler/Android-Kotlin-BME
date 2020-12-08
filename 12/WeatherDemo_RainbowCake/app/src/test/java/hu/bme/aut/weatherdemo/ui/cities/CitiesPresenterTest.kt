package hu.bme.aut.weatherdemo.ui.cities

import com.google.common.truth.Truth.assertThat
import hu.bme.aut.weatherdemo.createDomainCity
import hu.bme.aut.weatherdemo.createUiCity
import hu.bme.aut.weatherdemo.domain.interactors.CityInteractor
import hu.bme.aut.weatherdemo.domain.models.DomainCity
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CitiesPresenterTest : BehaviorSpec({

    val cityInteractor: CityInteractor = mockk()

    val citiesPresenter = CitiesPresenter(cityInteractor)

    Given("no preconditions") {
        every { cityInteractor.saveCity("Test") } returns createDomainCity(
            cityId = 0,
            cityName = "Test"
        )
        every { cityInteractor.deleteCity(createDomainCity(cityId = 1)) } returns Unit

        When("saveCity is called") {
            val result = citiesPresenter.saveCity("Test")

            Then("saved city is returned as UiCity") {
                verify { cityInteractor.saveCity("Test") }
                assertThat(result.cityName).contains("Test")
            }
        }
        When("deleteCity is called") {
            citiesPresenter.deleteCity(createUiCity(cityId = 1))

            Then("interactor is called with correct parameter") {
                verify { cityInteractor.deleteCity(createDomainCity(cityId = 1)) }
            }
        }
    }

    Given("two cities are saved") {
        every { cityInteractor.getSavedCities() } returns listOf(
            createDomainCity(cityId = 0, cityName = "  Whitespace around  "),
            createDomainCity(cityId = 1, cityName = "")
        )

        When("getSavedCities is called") {
            val result = citiesPresenter.getCities()

            Then("two UiCity is returned") {
                assertThat(result).hasSize(2)
            }
            Then("city name is trimmed") {
                assertThat(result[0].cityName).contains("Whitespace around")
            }
        }
    }

})
