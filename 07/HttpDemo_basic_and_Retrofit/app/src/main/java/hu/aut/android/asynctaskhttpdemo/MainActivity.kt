package hu.aut.android.asynctaskhttpdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.json.JSONException
import org.json.JSONObject
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.support.v4.content.LocalBroadcastManager
import android.content.IntentFilter
import android.widget.Toast
import hu.aut.android.asynctaskhttpdemo.retrofit.CurrencyExchangeAPI
import hu.aut.android.asynctaskhttpdemo.retrofit.MoneyResult
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.R.attr.name
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private val URL_BASE = "https://api.exchangeratesapi.io/latest?base=EUR"

    private val brWeatherReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val rawResult = intent.getStringExtra(
                HttpGetTask.KEY_RESULT
            )

            try {
                val rawJson = JSONObject(rawResult)
                val hufValue = rawJson.getJSONObject("rates").getString("HUF")
                tvResult.text = hufValue

            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }

    //https://api.exchangeratesapi.io/latest?base=EUR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.exchangeratesapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val currencyAPI = retrofit.create(CurrencyExchangeAPI::class.java)

        btnGetRate.setOnClickListener {
            //HttpGetTask(applicationContext).execute(URL_BASE)

            /*HttpGetTaskWithCalback { result ->
                try {
                    val rawJson = JSONObject(result)
                    val hufValue = rawJson.getJSONObject("rates").getString("HUF")
                    tvResult.text = hufValue

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }*/


            /*Thread {
                var data = OkHttpHelper.getRates()
                runOnUiThread{
                    Toast.makeText(this@MainActivity, data, Toast.LENGTH_LONG).show()
                }
            }.start()*/


            val ratesCall = currencyAPI.getRates("EUR")
            ratesCall.enqueue(object: Callback<MoneyResult> {
                override fun onFailure(call: Call<MoneyResult>, t: Throwable) {
                    tvResult.text = t.message
                }

                override fun onResponse(call: Call<MoneyResult>, response: Response<MoneyResult>) {
                    tvResult.text = response.body()?.rates?.HUF.toString()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            brWeatherReceiver,
            IntentFilter(HttpGetTask.FILTER_RESULT)
        )
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(
            this
        ).unregisterReceiver(brWeatherReceiver)
    }
}

