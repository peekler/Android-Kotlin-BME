package hu.bme.aut.weatherdemo.view.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import hu.bme.aut.weatherdemo.BuildConfig
import hu.bme.aut.weatherdemo.R
import hu.bme.aut.weatherdemo.model.network.WeatherResult
import hu.bme.aut.weatherdemo.network.WeatherAPI
import kotlinx.android.synthetic.main.activity_weather_details.*
import kotlinx.android.synthetic.main.city_row.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class WeatherDetailsActivity : AppCompatActivity() {

    companion object {
        const val KEY_CITY = "KEY_CITY"
    }

    private lateinit var cityName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_details)

        cityName = intent.getStringExtra(KEY_CITY)!!
        tvCity.text = cityName
    }

    override fun onResume() {
        super.onResume()

        getWeatherData()
    }

    private fun getWeatherData() {
        val weatherCall = prepareCall()

        weatherCall.enqueue(object : Callback<WeatherResult> {
            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                tvCityName.text = t.message
            }

            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                val weatherData = response.body()
                val icon = weatherData?.weather?.get(0)?.icon
                processResponse(weatherData, icon)
            }
        })
    }

    private fun processResponse(
        weatherData: WeatherResult?,
        icon: String?
    ) {
        Glide.with(this@WeatherDetailsActivity)
            .load("https://openweathermap.org/img/w/$icon.png")
            .into(ivWeatherIcon)


        tvMain.text = weatherData?.weather?.get(0)?.main
        tvDescription.text = weatherData?.weather?.get(0)?.description
        tvTemperature.text =
            getString(R.string.temperature, weatherData?.main?.temp?.toFloat().toString())

        val sdf = SimpleDateFormat("h:mm a z", Locale.getDefault())
        val sunriseDate = Date((weatherData?.sys?.sunrise?.toLong())!! * 1000)
        val sunriseTime = sdf.format(sunriseDate)
        tvSunrise.text = getString(R.string.sunrise, sunriseTime)
        val sunsetDate = Date(weatherData.sys.sunset?.toLong()!! * 1000)
        val sunsetTime = sdf.format(sunsetDate)
        tvSunset.text = getString(R.string.sunset, sunsetTime)
    }

    private fun prepareCall(): Call<WeatherResult> {
        var retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val weatherApi = retrofit.create(WeatherAPI::class.java)

        val weatherCall =
            weatherApi.getWeatherData(
                cityName,
                "metric",
                BuildConfig.WEATHER_API_KEY
            )
        return weatherCall
    }
}