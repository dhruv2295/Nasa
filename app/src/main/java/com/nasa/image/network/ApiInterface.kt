package com.nasa.image.network

import com.nasa.image.dataModel.NasaResponse
import com.nasa.image.utils.AppConstraint.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiInterface {

    @GET("planetary/apod")
    fun getApod(@Query("api_key") apiKey: String,
                @Query("start_date") startDate: String,
                @Query("end_date") endDate: String) : Call<List<NasaResponse>>
}

object NetworkService{
    var apiInterface: ApiInterface
    private val interceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    init {
        val httpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)

        val retrofit: Retrofit  = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        apiInterface = retrofit.create(ApiInterface::class.java)
    }
}