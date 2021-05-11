package com.example.sampleapp.views.homefrag

import com.example.sampleapp.R
import com.example.sampleapp.databinding.FragmentHomeBinding
import com.example.sampleapp.dataprovider.MarkerData
import com.example.sampleapp.utils.SessionManager
import com.example.sampleapp.views.main.MainInterface
import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import timber.log.Timber
import java.io.IOException
import java.util.*

class HomeFragment : Fragment() {

    private var mMap: GoogleMap? = null

    var latitude = 0.0
    var longitude = 0.0


    private lateinit var binding: FragmentHomeBinding
    private var mainView: View? = null
    private lateinit var callback: MainInterface
    private lateinit var markerPosition: Marker
    private var markerList: ArrayList<MarkerData> = ArrayList()

    companion object {
        fun newInstance() = HomeFragment()
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
        val loc = SessionManager.getLocation()
        Timber.d("location ${loc.latitude}//${loc.longitude}")
        if (loc != null) {
            latitude = loc.latitude!!
            longitude = loc.longitude!!
        }
        Timber.d("locationdata home$latitude//$longitude")
        markerList =
            SessionManager.getListObject("marker", MarkerData::class.java) as ArrayList<MarkerData>
        Timber.d("markerList start $markerList")
        setupBinding(inflater, parent)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync {
            mMap = it
        }

        setMap()
        onClick()
        return mainView
    }


    private fun onClick() {
        binding.info.setOnClickListener {
            Timber.d("latLong $latitude// $longitude")
            callback.setPage(1, LatLng(latitude, longitude))
            slideDown(binding.layout1)
        }
        binding.delete.setOnClickListener {
            val pos = markerList.indexOf(
                MarkerData(
                    LatLng(
                        markerPosition.position.latitude,
                        markerPosition.position.longitude
                    )
                )
            )

            markerPosition.remove()
            markerList.removeAt(pos)
            SessionManager.setMarkerList("marker", markerList)
            slideDown(binding.layout1)
            Timber.d("markerList remove $markerList")
        }
        binding.cancel.setOnClickListener {
            slideDown(binding.layout1)
        }
    }


    @SuppressLint("PotentialBehaviorOverride")
    private fun setMap() {
        Timber.d("valuesfromlocation $latitude// $longitude")
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync {
            mMap = it

            getPosition(LatLng(latitude, longitude))

            if (markerList.size != 0) {
                markerList.forEach { m ->
                    mMap?.addMarker(
                        MarkerOptions()
                            .position(m.marker)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))

                    )
                }
            }

            mMap?.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
                override fun onMapClick(p0: LatLng) {
                    val address = getAddress(p0)
                    mMap?.addMarker(
                        MarkerOptions()
                            .position(p0)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                            .title(address)
                    )
                    markerList.add(MarkerData(p0))
                    SessionManager.setMarkerList("marker", markerList)
                    Timber.d("markerList add $markerList")

//
                }

            })
            mMap?.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                override fun onMarkerClick(p0: Marker): Boolean {
                    markerPosition = p0
                    latitude = p0.position.latitude
                    longitude = p0.position.longitude
                    val address = getAddress(p0.position)
                    binding.title.text = address
                    showView()
                    return true
                }
            })


        }
    }

    @SuppressLint("SetTextI18n")
    private fun getAddress(position: LatLng): String {
        val geocoder = Geocoder(activity, Locale.getDefault())

        try {
            val addressList: List<Address>? = geocoder.getFromLocation(
                position.latitude, position.longitude, 1
            )
            if (addressList != null && addressList.isNotEmpty()) {
                val address: Address = addressList[0]
                return address.getAddressLine(0)

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun getPosition(currentLoc: LatLng) {

        addZoom(currentLoc)
    }

    private fun addZoom(currentLoc: LatLng) {

        val zoomLevel = 18.0f //This goes up to 21
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, zoomLevel))

    }


    private fun setupBinding(inflater: LayoutInflater, parent: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, parent, false)
        binding.lifecycleOwner = this
        mainView = binding.root

    }

    private fun showView() {
        Timber.d("detailClicked showview")
        slideUp(binding.layout1)
    }

    private fun slideUp(view: View) {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            0F,
            0F,
            view.height.toFloat(),
            0F
        )
        animate.duration = 250
        animate.fillAfter = true
        view.startAnimation(animate)

    }

    private fun slideDown(view: View) {
        val animate = TranslateAnimation(
            0F,
            0F,
            0F,
            view.height.toFloat()
        )
        animate.duration = 250
        animate.fillAfter = true
        view.startAnimation(animate)
        view.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        if (SessionManager.getReset()) {
            mMap?.clear()
            SessionManager.editor.remove("marker")
            markerList.clear()
            SessionManager.setReset(false)
        }

    }

}