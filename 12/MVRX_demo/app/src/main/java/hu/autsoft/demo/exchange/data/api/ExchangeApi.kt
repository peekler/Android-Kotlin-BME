package hu.autsoft.demo.exchange.data.api

import hu.autsoft.demo.exchange.data.model.ExchangeData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeApi {

    @GET("/latest")
    fun getRates(@Query("base") base: String): Call<ExchangeData>
}