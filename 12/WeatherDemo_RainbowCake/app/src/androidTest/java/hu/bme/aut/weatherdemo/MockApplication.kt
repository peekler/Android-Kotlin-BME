package hu.bme.aut.weatherdemo

import hu.bme.aut.weatherdemo.di.ApplicationModule

class MockApplication : WeatherDemoApplication() {

    override fun setupInjector() {
        injector = DaggerAppTestComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }
}