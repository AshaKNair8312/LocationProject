package com.example.sampleapp.views.cityfrag

import com.example.sampleapp.R
import com.example.sampleapp.databinding.FragmentCityBinding
import com.example.sampleapp.dataprovider.WeatherResponse
import com.example.sampleapp.network.ConnectionManager
import com.example.sampleapp.network.ResultCallBack
import com.example.sampleapp.views.main.MainInterface
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class CityFragment : Fragment() {

    private lateinit var binding: FragmentCityBinding
    private var mainView: View? = null
    private lateinit var callback: MainInterface
    private var lat:Double=0.0
    private var lng:Double=0.0

    private val viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    companion object {
        fun newInstance() = CityFragment()
    }

    override fun onAttach(context: Context) {
        if (activity is MainInterface) {
            callback = activity as MainInterface
        }
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val info: LatLng = callback.getData() as LatLng
        lat= info.latitude
        lng= info.longitude
        if(lat==0.0 &&lng==0.0) {
            lat = 9.9312
            lng = 76.2673
        }
        Timber.d("city $lat,$lng")
        getWeatherApi(lat, lng)

        setupBinding(inflater, parent)

        return mainView
    }

    private fun getWeatherApi(lat: Double, lng: Double) {
        if (isInternetAvailable()) {
            coroutineScope.launch {
                ConnectionManager.getDataManager().getWeather(lat, lng, object :
                    ResultCallBack<WeatherResponse> {
                    override fun onError(code: Int, errorMessage: String) {
                        Timber.d("WeatherResponse-> $errorMessage")
                        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
                    }

                    override fun <T> onSuccess(response: T) {
                        Timber.d("WeatherResponse $response")
                        if (response != null) {
                            val resp = response as WeatherResponse
                            binding.viewModel = resp

                        }

                    }
                })
            }
        } else {
            Toast.makeText(activity, "Check your internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    fun isInternetAvailable(): Boolean {
        val result: Boolean
        val connectivityManager = activity?.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting == true
        }

        return result
    }


    fun getData(latLng: LatLng) {
        Timber.d("LatLong $latLng")
    }

    private fun setupBinding(inflater: LayoutInflater, parent: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_city, parent, false)
        binding.lifecycleOwner = this
        mainView = binding.root

    }

    override fun onResume() {
        super.onResume()
        getWeatherApi(lat, lng)

    }

}