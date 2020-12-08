package hu.bme.aut.weatherdemo.ui.citydetails

import android.os.Bundle
import android.view.View
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.bumptech.glide.Glide
import hu.bme.aut.weatherdemo.R
import kotlinx.android.synthetic.main.fragment_city_details.*
import timber.log.Timber

class CityDetailsFragment : RainbowCakeFragment<CityDetailsViewState, CityDetailsViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_city_details

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        var cityName = requireArguments().getString("cityName")!!
        tvCity.text = cityName

        viewModel.load(cityName)
    }

    override fun render(viewState: CityDetailsViewState) {
        when (viewState) {
            is Loading -> Timber.d("Loading...")
            is CityDetailsReady -> {
                Glide.with(this)
                    .load("https://openweathermap.org/img/w/${viewState.weatherData.icon}.png")
                    .into(ivWeatherIcon)

                tvMain.text = viewState.weatherData.main
                tvDescription.text = viewState.weatherData.description
                tvTemperature.text =
                    getString(R.string.temperature, viewState.weatherData.temperature)
                tvSunrise.text = getString(R.string.sunrise, viewState.weatherData.sunrise)
                tvSunset.text = getString(R.string.sunset, viewState.weatherData.sunset)
            }
            is Error -> Timber.d("Error loading")
        }
    }

}
