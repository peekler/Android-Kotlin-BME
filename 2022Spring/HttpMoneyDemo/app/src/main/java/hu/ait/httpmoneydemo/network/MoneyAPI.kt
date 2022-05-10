package hu.ait.httpmoneydemo.network

import hu.ait.httpmoneydemo.data.MoneyResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// http://data.fixer.io/api/latest?access_key=969c37b5a73f8cb2d12c081dcbdc35e6

// HOST: http://data.fixer.io
// PATH: /api/latest
// ? : this is where the query arguments start
// Query arguments:
// - access_key

interface MoneyAPI {

    @GET("/api/latest")
    fun getMoneyRates(@Query("access_key") accessKey: String) : Call<MoneyResult>

}