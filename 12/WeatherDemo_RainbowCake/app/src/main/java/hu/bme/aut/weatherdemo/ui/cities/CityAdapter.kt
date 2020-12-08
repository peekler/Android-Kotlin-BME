package hu.bme.aut.weatherdemo.ui.cities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.weatherdemo.R
import hu.bme.aut.weatherdemo.ui.cities.CityAdapter.CityViewHolder
import hu.bme.aut.weatherdemo.ui.cities.models.UiCity
import kotlinx.android.synthetic.main.city_row.view.*

class CityAdapter : ListAdapter<UiCity, CityViewHolder>(CityComparator) {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_row, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = getItem(position)

        holder.city = city
        holder.tvCityName.text = city.cityName
    }

    inner class CityViewHolder(cityView: View) : RecyclerView.ViewHolder(cityView) {
        val tvCityName: TextView = cityView.tvCityName
        val cardView: CardView = cityView.cardView
        val btnDelete: Button = cityView.btnDelete

        var city: UiCity? = null

        init {
            btnDelete.setOnClickListener {
                city?.let { listener?.onDeleteClicked(it) }
            }

            cardView.setOnClickListener {
                city?.let { listener?.onCityClicked(it) }
            }
        }
    }

    interface Listener {
        fun onCityClicked(city: UiCity)
        fun onDeleteClicked(city: UiCity)
    }
}