package hu.bme.aut.weatherdemo

import co.zsmb.rainbowcake.dagger.RainbowCakeModule
import dagger.Component
import hu.bme.aut.weatherdemo.data.disk.DiskModule
import hu.bme.aut.weatherdemo.data.network.NetworkModule
import hu.bme.aut.weatherdemo.di.AppComponent
import hu.bme.aut.weatherdemo.di.ApplicationModule
import hu.bme.aut.weatherdemo.di.ViewModelModule
import hu.bme.aut.weatherdemo.list.CitiesTest
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        RainbowCakeModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        DiskModule::class
    ]
)
interface AppTestComponent : AppComponent {

    fun inject(test: CitiesTest)
}