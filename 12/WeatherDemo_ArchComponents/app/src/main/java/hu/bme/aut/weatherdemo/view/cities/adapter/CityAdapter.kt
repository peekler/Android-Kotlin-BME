package hu.bme.aut.weatherdemo.view.cities.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.weatherdemo.R
import hu.bme.aut.weatherdemo.view.weather.WeatherDetailsActivity
import hu.bme.aut.weatherdemo.database.AppDatabase
import hu.bme.aut.weatherdemo.databinding.CityRowBinding
import hu.bme.aut.weatherdemo.model.db.City
import hu.bme.aut.weatherdemo.viewmodel.CitiesViewModel
import kotlinx.android.synthetic.main.city_row.view.*
import kotlin.concurrent.thread

class CityAdapter(private val context: Context,
                  private val citiesViewModel: CitiesViewModel) : ListAdapter<City, CityAdapter.ViewHolder>(CityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding: CityRowBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.city_row,
            parent, false)
        binding.adapter = this
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = getItem(position)
        holder.bind(city)
    }

    fun deleteCity(city: City) {
        citiesViewModel.delete(city)
    }

    fun showDetails(city: City) {
        val intent = Intent(context, WeatherDetailsActivity::class.java)
        intent.putExtra(WeatherDetailsActivity.KEY_CITY, city.name)
        context.startActivity(intent)
    }

    class ViewHolder(val binding: CityRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(city: City) {
            binding.city = city
        }
    }
}

class CityDiffCallback : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem.cityId == newItem.cityId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem == newItem
    }
}