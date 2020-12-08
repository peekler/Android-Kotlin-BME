package hu.autsoft.demo.exchange.data.interactor

import hu.autsoft.demo.exchange.data.api.ExchangeApi
import hu.autsoft.demo.exchange.data.model.ExchangeData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ExchangeInteractor {

    private var retrofit: Retrofit? = null

    fun getExchangeData(
        baseCurrency: String,
        onResponse: (ExchangeData) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("https://api.exchangeratesapi.io")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }

        val exchangeApi = retrofit!!.create(ExchangeApi::class.java)

        exchangeApi.getRates(baseCurrency).enqueue(object : Callback<ExchangeData> {
            override fun onResponse(call: Call<ExchangeData>, response: Response<ExchangeData>) {
                onResponse(response.body()!!)
            }

            override fun onFailure(call: Call<ExchangeData>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}