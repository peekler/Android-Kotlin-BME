package hu.ait.httpmoneydemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.ait.httpmoneydemo.data.MoneyResult
import hu.ait.httpmoneydemo.databinding.ActivityMainBinding
import hu.ait.httpmoneydemo.network.MoneyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://data.fixer.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val moneyService = retrofit.create(MoneyAPI::class.java)

        binding.btnGetRates.setOnClickListener {
            val call = moneyService.getMoneyRates("969c37b5a73f8cb2d12c081dcbdc35e6")
            call.enqueue(object : Callback<MoneyResult> {
                override fun onResponse(call: Call<MoneyResult>, response: Response<MoneyResult>) {
                    binding.tvData.text = "EUR-HUF: ${response.body()!!.rates!!.HUF}\n" +
                            "EUR-USD: ${response.body()!!.rates!!.USD}"
                }

                override fun onFailure(call: Call<MoneyResult>, t: Throwable) {
                    binding.tvData.text = "Error: ${t.message}"
                }
            })
        }
    }

}