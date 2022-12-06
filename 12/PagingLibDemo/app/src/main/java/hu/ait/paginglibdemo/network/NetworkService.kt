package hu.ait.paginglibdemo.network

import hu.ait.paginglibdemo.BuildConfig
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import hu.ait.paginglibdemo.data.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

interface NetworkService {

    @GET("everything?q=sports&apiKey=${BuildConfig.API_KEY}")
    fun getNews(@Query("page") page: Int, @Query("pageSize") pageSize: Int): Single<Response>

    companion object {
        fun getService(): NetworkService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(NetworkService::class.java)
        }
    }
}