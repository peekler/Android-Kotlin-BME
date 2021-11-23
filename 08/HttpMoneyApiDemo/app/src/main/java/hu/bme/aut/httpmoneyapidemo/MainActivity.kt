package hu.bme.aut.httpmoneyapidemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.httpmoneyapidemo.data.MoneyResult
import hu.bme.aut.httpmoneyapidemo.network.MoneyAPI
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var retrofit: Retrofit
    lateinit var moneyAPI: MoneyAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        retrofit = Retrofit.Builder()
            .baseUrl("http://data.fixer.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        moneyAPI = retrofit.create(MoneyAPI::class.java)

        btnGetMoney.setOnClickListener {
            val call = moneyAPI.getRates("969c37b5a73f8cb2d12c081dcbdc35e6")
            call.enqueue(object: Callback<MoneyResult> {
                override fun onResponse(call: Call<MoneyResult>, response: Response<MoneyResult>) {
                    tvData.text = response.body()!!.rates!!.HUF.toString()
                }

                override fun onFailure(call: Call<MoneyResult>, t: Throwable) {
                    tvData.text = t.message
                }

            })
        }

    }
}