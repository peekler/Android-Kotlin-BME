package hu.bme.aut.weatherdemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import hu.bme.aut.weatherdemo.database.AppDatabase
import hu.bme.aut.weatherdemo.model.db.City
import hu.bme.aut.weatherdemo.repository.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class CitiesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : CityRepository
    val allCities: LiveData<List<City>>

    init {
        val citiesDao = AppDatabase.getInstance(application).cityDao()
        repository = CityRepository(citiesDao)
        allCities = repository.allCities
    }

    fun insert(city: City) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(city)
    }

    fun delete(city: City) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(city)
    }

}