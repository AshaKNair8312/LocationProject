package com.example.sampleapp.views.splash

import com.example.sampleapp.R
import com.example.sampleapp.dataprovider.LocationData
import com.example.sampleapp.utils.SessionManager
import com.example.sampleapp.views.main.MainActivity
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mumayank.com.airlocationlibrary.AirLocation
import timber.log.Timber
import java.util.ArrayList

class TempActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)
        airLocation.start()

    }

    private fun callIntent() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }

    private val airLocation = AirLocation(this, object : AirLocation.Callback {
        override fun onSuccess(locations: ArrayList<Location>) {
            var string = "\n"
            for (location in locations) {
                string = "${location.longitude}, ${location.latitude}\n$string"

                SessionManager.setLocation(LocationData(location.latitude, location.longitude))
                callIntent()
                break
            }

            Timber.d("locationString--> $string")
        }

        override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
            Timber.d("locationString--> ${locationFailedEnum.name}")
            SessionManager.setLocation(LocationData(9.9312, 76.2673))
            callIntent()
        }
    }, true)

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        airLocation.onActivityResult(requestCode, resultCode, data)
    }
}