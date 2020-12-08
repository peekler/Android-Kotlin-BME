package hu.bme.aut.weatherdemo.ui.cities

import androidx.recyclerview.widget.DiffUtil
import hu.bme.aut.weatherdemo.ui.cities.models.UiCity

object CityComparator : DiffUtil.ItemCallback<UiCity>() {

    override fun areItemsTheSame(oldItem: UiCity, newItem: UiCity): Boolean {
        return oldItem.cityId == newItem.cityId
    }

    override fun areContentsTheSame(oldItem: UiCity, newItem: UiCity): Boolean {
        return oldItem == newItem
    }
}