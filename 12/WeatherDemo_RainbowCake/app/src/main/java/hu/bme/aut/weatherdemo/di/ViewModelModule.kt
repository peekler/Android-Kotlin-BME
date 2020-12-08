package hu.bme.aut.weatherdemo.di

import androidx.lifecycle.ViewModel
import co.zsmb.rainbowcake.dagger.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import hu.bme.aut.weatherdemo.ui.cities.CitiesViewModel
import hu.bme.aut.weatherdemo.ui.citydetails.CityDetailsViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CitiesViewModel::class)
    abstract fun bindCitiesViewModel(viewModel: CitiesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CityDetailsViewModel::class)
    abstract fun bindCityDetailsViewModel(viewModel: CityDetailsViewModel): ViewModel
}