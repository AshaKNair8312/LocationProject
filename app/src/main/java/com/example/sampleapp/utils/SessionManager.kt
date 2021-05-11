package com.example.sampleapp.utils

import com.example.sampleapp.dataprovider.LocationData
import com.example.sampleapp.dataprovider.MarkerData
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import timber.log.Timber
import java.util.ArrayList

class SessionManager {

    companion object {
        lateinit var sharedPref: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
        lateinit var context: Context

        private val PREF_NAME = "Homely"
        private val PRIVATE_MODE = 0
        private val RESET = "reset"
        private val UNIT_SYSTEM = "unitSystem"
        private val MARKER = "marker"
        private val RADIO = "radio"
        private val lat = "lat"
        private val lng = "lng"


        @SuppressLint("CommitPrefEdits")
        fun initializeval(context: Context) {
            this.context = context
            sharedPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
            editor = sharedPref.edit()
        }

        fun clear() {
            editor.clear()
            editor.apply()
        }

        fun setReset(b: Boolean) {
            editor.putBoolean(RESET, b)
            editor.apply()
        }

        fun getReset(): Boolean {
            return sharedPref.getBoolean(RESET, false)
        }

        fun setUnitSystem(s: String) {
            editor.putString(UNIT_SYSTEM, s)
            editor.apply()
        }

        fun getUnitSystem(): String? {
            return sharedPref.getString(UNIT_SYSTEM, "")
        }

        fun setMarkers(s: String) {

        }

        fun setRadio(s: String) {
            editor.putString(RADIO, s)
            editor.apply()
        }

        fun getRadio(): String? {
            return sharedPref.getString(RADIO, "")
        }

        fun setMarkerList(key: String?, objArray: ArrayList<MarkerData>) {
            val gson = Gson()
            val objStrings = ArrayList<String>()
            for (obj in objArray) {
                objStrings.add(gson.toJson(obj))
            }
            putListString(key, objStrings)
        }

//        fun getCartList(key: String?, mClass: Class<MarkerData>): ArrayList<MarkerData> {
//            val gson = Gson()
//            val objStrings = getListObject(key,MarkerData::class.java)
//            val objects = ArrayList<MarkerData>()
//            for (jObjString com objStrings) {
//                val value = gson.fromJson(jObjString,mClass)
//                objects.add(value)
//            }
//            return objects
//        }

        fun putListString(key: String?, stringList: ArrayList<String>) {
            val myStringList = stringList.toTypedArray()
            sharedPref.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply()
        }

        fun getListObject(key: String?, mClass: Class<*>?): ArrayList<Any> {
            val gson = Gson()
            val objStrings = getListString(key)
            val objects = ArrayList<Any>()
            for (jObjString in objStrings) {
                val value = gson.fromJson(jObjString, mClass)
                objects.add(value)
            }
            return objects
        }

        fun getListString(key: String?): ArrayList<String> {
            return ArrayList(listOf(*TextUtils.split(sharedPref.getString(key, ""), "‚‗‚")))
        }

        fun setLocation(location: LocationData) {
            Timber.d("locationData com${location.latitude.toString()}")
            editor.putString("lat", location.latitude.toString())
            editor.putString("lng", location.longitude.toString())
            editor.apply()
        }

        fun getLocation(): LocationData {
            Timber.d("locationData out${sharedPref.getString("lat", "")}")
            val lat = sharedPref.getString("lat", "")
            val lng = sharedPref.getString("lng", "")
            if (lat != "" && lng != "")
                return LocationData(lat?.toDouble(), lng?.toDouble())
            return LocationData(0.0,0.0)
        }
    }
}