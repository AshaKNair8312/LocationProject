package com.example.sampleapp.network

import com.example.sampleapp.dataprovider.WeatherResponse
import com.example.sampleapp.network.RetroApi.retrofitService
import com.example.sampleapp.utils.Constants
import kotlinx.coroutines.withTimeout
import retrofit2.Response
import timber.log.Timber

class ConnectionManager {
    companion object {
        fun getDataManager(): ConnectionManager {
            return ConnectionManager()
        }
    }

    suspend fun getWeather(
        lat:Double,
        lng:Double,
        callBack: ResultCallBack<WeatherResponse>
    ) {
        val job = withTimeout(Constants.TIMEOUT.toLong()) {

                val getPropertiesDeferred = retrofitService.getWeatherAsync(lat,lng,Constants.API_KEY)
            Timber.d("weatherData response$getPropertiesDeferred")
                try {
                    val response = getPropertiesDeferred.await()
                    Timber.d("weatherData response${response.body()}")
                    onResponseReceived(response, callBack)
                } catch (e: Exception) {
                    e.message?.let { callBack.onError(1, it) }
                }

        }
        if (job == null)
            callBack.onError(2, "Connection timed out")
    }



    private fun <T> onResponseReceived(response: Response<T>, callBack: ResultCallBack<T>) {
        if (response.isSuccessful) {
            if (response.body() != null) {
                callBack.onSuccess(response.body())
            }

        } else {
            callBack.onError(1, response.message())
        }
    }

}