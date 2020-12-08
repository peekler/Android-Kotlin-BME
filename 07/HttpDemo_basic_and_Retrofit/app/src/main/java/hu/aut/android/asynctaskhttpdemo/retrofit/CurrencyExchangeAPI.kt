package hu.aut.android.asynctaskhttpdemo.retrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.exchangeratesapi.io/
//
// latest?base=USD

interface CurrencyExchangeAPI {
    @GET("/latest")
    fun getRates(@Query("base") base: String): Call<MoneyResult>
}
