package hu.bme.aut.weatherdemo.ui.cities

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import hu.bme.aut.weatherdemo.R
import hu.bme.aut.weatherdemo.ui.cities.CitiesViewModel.ShowAddCityDialog
import hu.bme.aut.weatherdemo.ui.cities.models.UiCity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_cities.*
import timber.log.Timber

class CitiesFragment : RainbowCakeFragment<CitiesViewState, CitiesViewModel>(), CityAdapter.Listener {

    private lateinit var adapter: CityAdapter

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_cities

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CityAdapter()
        adapter.listener = this
        listCities.adapter = adapter

        fab.setOnClickListener {
            viewModel.showDialog()
        }

        toolbar.title = getString(R.string.app_name)
    }

    override fun onStart() {
        super.onStart()

        viewModel.load()
    }

    override fun render(viewState: CitiesViewState) {
        when (viewState) {
            is Loading -> Timber.d("Loading...")
            is CitiesReady -> adapter.submitList(viewState.cities)
        }
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is ShowAddCityDialog -> {
                MaterialDialog(requireContext()).show {
                    noAutoDismiss()
                    title(text = "Add City Name")
                    input()

                    positiveButton(text = "Add") {
                        val cityName = it.getInputField().text.toString()
                        if (cityName.isNotEmpty()) {
                            viewModel.saveCity(cityName)
                            it.dismiss()
                        } else {
                            it.getInputField().error = "Required"
                        }
                    }
                    negativeButton(text = "Dismiss") {
                        it.dismiss()
                    }
                }
            }
        }
    }

    override fun onCityClicked(city: UiCity) {
        findNavController().navigate(
            CitiesFragmentDirections.actionCitiesFragmentToCityDetailsFragment(city.cityName)
        )
    }

    override fun onDeleteClicked(city: UiCity) {
        viewModel.deleteCity(city)
    }
}
