package hu.aut.android.asynctaskhttpdemo

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object OkHttpHelper{
    fun getRates():String{
        val client=OkHttpClient.Builder()
            .connectTimeout(5000,TimeUnit.MILLISECONDS)
            .build()

        val request=Request.Builder()
            .url("https://api.exchangeratesapi.io/latest?base=EUR")
            .get()
            .build()

        val call=client.newCall(request)
        val response=call.execute()

        val responseStr=response.body()!!.string()
        return  responseStr
    }
}