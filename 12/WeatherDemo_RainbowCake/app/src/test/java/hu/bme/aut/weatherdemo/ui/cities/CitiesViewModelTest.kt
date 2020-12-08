package hu.bme.aut.weatherdemo.ui.cities

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.observeStateAndEvents
import hu.bme.aut.weatherdemo.ViewModelTestListener
import hu.bme.aut.weatherdemo.createUiCity
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CitiesViewModelTest : StringSpec({

    val citiesPresenter: CitiesPresenter = mockk()

    "load should get cities from presenter" {
        val viewModel = CitiesViewModel(citiesPresenter)
        viewModel.observeStateAndEvents { stateObserver, _ ->
            coEvery { citiesPresenter.getCities() } returns listOf(createUiCity())

            viewModel.load()

            stateObserver.assertObserved(
                Initial,
                Loading,
                CitiesReady(listOf(createUiCity()))
            )
        }
    }

    "saveCity refreshes the list after saving" {
        val viewModel = CitiesViewModel(citiesPresenter)
        viewModel.observeStateAndEvents { stateObserver, _ ->
            coEvery { citiesPresenter.saveCity(any()) } returns createUiCity()
            coEvery { citiesPresenter.getCities() } returns listOf(createUiCity())

            viewModel.saveCity("Test")

            stateObserver.assertObserved(
                Initial,
                Loading,
                CitiesReady(listOf(createUiCity()))
            )
        }
    }

    "deleteCity refreshes the list after deletion" {
        val viewModel = CitiesViewModel(citiesPresenter)
        viewModel.observeStateAndEvents { stateObserver, _ ->
            coEvery { citiesPresenter.deleteCity(any()) } returns Unit
            coEvery { citiesPresenter.getCities() } returns listOf()

            viewModel.deleteCity(createUiCity())

            stateObserver.assertObserved(
                Initial,
                Loading,
                CitiesReady(listOf())
            )
        }
    }
}) {
    init {
        listener(ViewModelTestListener())
    }

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        clearAllMocks()
    }
}
