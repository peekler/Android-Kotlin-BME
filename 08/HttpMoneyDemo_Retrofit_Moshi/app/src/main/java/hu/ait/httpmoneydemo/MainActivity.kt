package hu.ait.httpmoneydemo

import android.os.Bundle
import hu.ait.httpmoneydemo.data.MoneyResult
import hu.ait.httpmoneydemo.network.MoneyAPI
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.exchangeratesapi.io/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()


        val currencyAPI = retrofit.create(MoneyAPI::class.java)

        btnGetRates.setOnClickListener {

            val moneyCall = currencyAPI.getMoney("USD")

            moneyCall.enqueue(object : Callback<MoneyResult> {
                override fun onFailure(call: Call<MoneyResult>, t: Throwable) {
                    tvData.text = t.message
                }

                override fun onResponse(call: Call<MoneyResult>, response: Response<MoneyResult>) {
                    var moneyResult = response.body()

                    tvData.text = "HUF: ${moneyResult?.rates?.HUF}"
                }
            })

        }
    }
}
