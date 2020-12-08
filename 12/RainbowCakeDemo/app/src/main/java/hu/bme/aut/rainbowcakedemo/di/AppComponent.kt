package hu.bme.aut.rainbowcakedemo.di

import co.zsmb.rainbowcake.dagger.RainbowCakeComponent
import co.zsmb.rainbowcake.dagger.RainbowCakeModule
import dagger.Component
import hu.bme.aut.rainbowcakedemo.network.ExchangeRatesModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RainbowCakeModule::class,
        ViewModelModule::class,
        ApplicationModule::class,
        ExchangeRatesModule::class
    ]
)
interface AppComponent : RainbowCakeComponent