package com.example.sampleapp.network

import com.example.sampleapp.dataprovider.WeatherResponse
import com.example.sampleapp.utils.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

const val BASE_URL = Constants.BASE_URL

fun getClient(): Retrofit {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    val httpClient = OkHttpClient.Builder()
    httpClient.connectTimeout(Constants.CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
    httpClient.callTimeout(Constants.CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
    httpClient.readTimeout(Constants.CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
    httpClient.addInterceptor(interceptor)
    httpClient.addInterceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .method(original.method, original.body)
            .addHeader("Content-Type", "application/json")
            .addHeader("User-Agent", "android")
            .build()
        val response = chain.proceed(request)
        val rawJson = response.body?.string()
        response.newBuilder().body(rawJson?.toResponseBody(response.body?.contentType())).build()
    }

    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(httpClient.build())
        .baseUrl(BASE_URL)
        .build()
}

interface ApiService {
    @GET("data/2.5/weather")
    fun getWeatherAsync(
        @Query("lat")lat:Double,
        @Query("lon")lon:Double,
        @Query("appid")appid:String
    ):Deferred<Response<WeatherResponse>>
}

object RetroApi {
    val retrofitService: ApiService by lazy {
        getClient().create(ApiService::class.java)
    }
}