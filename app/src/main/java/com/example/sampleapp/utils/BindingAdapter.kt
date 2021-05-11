package com.example.sampleapp.utils

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("SetTextI18n")
@BindingAdapter("toCelsius")
fun setToCelsius(textView: TextView, temp: Double) {
    Timber.d("radioValue ${SessionManager.getRadio()}")

    var x=""
    when(SessionManager.getRadio()){
        "standard"->{
            x = temp.toString()
            textView.text = "${x.substringBefore(".")}K"
        }
        "metric"->{
            x=(temp - 273.15).toString()
            textView.text = "${x.substringBefore(".")}°C"
        }
        "imperial"->{
            x= (((9.div(5))*temp - 273) + 32).toString()
            textView.text = "${x.substringBefore(".")}°F"
        }
    }


}

@SuppressLint("SetTextI18n", "SimpleDateFormat")
@BindingAdapter("toHour", "sun")
fun setToHour(textView: TextView, millis: Long, s: String) {
    val hour = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(
        TimeUnit.MILLISECONDS.toDays(millis)
    );
    val min = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
        TimeUnit.MILLISECONDS.toHours(millis)
    );
    var time = ""
    time = if (min < 10)
        "$hour:0$min"
    else
        "$hour:$min"
    if (s.toLowerCase(Locale.getDefault()) == "sunset") {
        try {
            val format = SimpleDateFormat("HH:mm")
            val format1 = SimpleDateFormat("hh:mm a")
            val dateTime = format.parse(time)
            val newTime = format1.format(dateTime)
            textView.text = newTime.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    } else
        textView.text = "$time AM"

}

