package com.example.sampleapp.utils
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.location.LocationSettingsRequest
import android.location.LocationManager
import com.google.android.gms.location.LocationRequest
import android.app.Activity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.common.api.ResolvableApiException
import android.content.IntentSender.SendIntentException
import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.google.android.gms.location.LocationServices

class GpsUtils(private val context: Context) {
    private val mSettingsClient: SettingsClient = LocationServices.getSettingsClient(context)
    var location: Location? = null
    private val mLocationSettingsRequest: LocationSettingsRequest
    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val locationRequest: LocationRequest = LocationRequest.create()

    // method for turn on GPS
    fun turnGPSOn(onGpsListener: onGpsListener) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGpsListener.gpsStatus(true)
        } else {
            mSettingsClient.checkLocationSettings(mLocationSettingsRequest).addOnSuccessListener(
                (context as Activity)
            ) {
                //  GPS is already enable, callback GPS status through listener
                onGpsListener.gpsStatus(true)
            }.addOnFailureListener(context) { e ->

                when ((e as ApiException).statusCode) {

                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        // Show the dialog by calling startResolutionForResult(), and check the
                        // result com onActivityResult().
                        val resolvable = e as ResolvableApiException
                        startIntentSenderForResult(context,resolvable.resolution.intentSender,
                            111,
                            null, 0, 0, 0, null
                        )

//                        val rae = e as ResolvableApiException
//                        rae.startResolutionForResult(context, 111)

                    } catch (sie: SendIntentException) {

                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage =
                            "Location settings are inadequate, and cannot be " + "fixed here. Fix com Settings."

                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    val isGpsOn: Boolean
        get() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    interface onGpsListener {
        fun gpsStatus(isGPSEnable: Boolean)
    }

    init {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (10 * 1000).toLong()
        locationRequest.fastestInterval = (2 * 1000).toLong()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        mLocationSettingsRequest = builder.build()
        //**************************
        builder.setAlwaysShow(true) //this is the key ingredient
        //**************************
    }
}