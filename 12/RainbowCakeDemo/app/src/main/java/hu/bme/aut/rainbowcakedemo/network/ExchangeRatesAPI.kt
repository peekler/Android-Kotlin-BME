package hu.bme.aut.rainbowcakedemo.network

import hu.bme.aut.rainbowcakedemo.network.model.MoneyResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchnageRatesAPI  {

    @GET("/latest")
    suspend fun getMoney(@Query("base") base: String) : MoneyResult

}