package hu.bme.aut.httpmoneyapidemo.network

import hu.bme.aut.httpmoneyapidemo.data.MoneyResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// HOST: http://data.fixer.io
// PATH: /api/latest
// ?
// QUERY ARGs: access_key=969c37b5a73f8cb2d12c081dcbdc35e6

interface MoneyAPI {

    @GET("/api/latest")
    fun getRates(@Query("access_key") key: String) : Call<MoneyResult>

}