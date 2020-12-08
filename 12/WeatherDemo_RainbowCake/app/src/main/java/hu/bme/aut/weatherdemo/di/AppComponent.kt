package hu.bme.aut.weatherdemo.di

import co.zsmb.rainbowcake.dagger.RainbowCakeComponent
import co.zsmb.rainbowcake.dagger.RainbowCakeModule
import dagger.Component
import hu.bme.aut.weatherdemo.data.disk.DiskModule
import hu.bme.aut.weatherdemo.data.network.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        DiskModule::class,
        NetworkModule::class,
        RainbowCakeModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : RainbowCakeComponent