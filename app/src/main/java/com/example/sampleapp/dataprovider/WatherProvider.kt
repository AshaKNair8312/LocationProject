package com.example.sampleapp.dataprovider

import com.google.android.gms.maps.model.LatLng

data class WeatherResponse(
    val coord:CordData?,
    val weather:List<WeatherData>?,
    val base:String?,
    val main:MainData?,
    val visibility:Long?,
    val wind:WindData?,
    val clouds:CloudData?,
    val dt:Long?,
    val sys:SysData?,
    val timezone:Int?,
    val id:Int?,
    val name:String?,
    val cod:Int?
)

data class CordData(
    val lon:Double?,
    val lat:Double?
)

data class WeatherData(
    val main:String?,
    val description:String?,
)

data class MainData(
    val temp:Double?,
    val feels_like:Double?,
    val temp_min:Double?,
    val temp_max:Double?,
    val pressure:Double?,
    val humidity:Double?,
    val sea_level:Double?,
    val grnd_level:Double?
)

data class WindData(
    val speed:Double?
)

data class CloudData(
    val all:Int?
)
data class SysData(
    val country:String?,
    val sunrise:Long?,
    val sunset:Long?
)

data class MarkerData(
    val marker:LatLng?
)

data class LocationData(
    val latitude:Double?,
    val longitude:Double?
)