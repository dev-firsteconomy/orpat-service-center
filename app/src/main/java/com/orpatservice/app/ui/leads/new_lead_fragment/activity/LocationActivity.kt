package com.orpatservice.app.ui.leads.new_lead_fragment.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.orpatservice.app.R
import com.orpatservice.app.utils.Constants

class LocationActivity : AppCompatActivity(), OnMapReadyCallback {
    private var latitude = ""
    private var longitude = ""
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        latitude = intent.getStringExtra(Constants.LATITUDE).toString()
        longitude = intent.getStringExtra(Constants.LONGITUDE).toString()


        val mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap


      /*  val latLngOrigin = LatLng(10.3181466, 123.9029382) // Ayala
        val latLngDestination = LatLng(latitude.toDouble(),longitude.toDouble()) // SM City
        this.googleMap!!.addMarker(MarkerOptions().position(latLngOrigin).title("Ayala"))
        this.googleMap!!.addMarker(MarkerOptions().position(latLngDestination).title("SM City"))
        this.googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 14.5f))*/

        val latLngOrigin = LatLng(10.3181466, 123.9029382) // Ayala
        val latLngDestination = LatLng(10.311795,123.915864) // SM City
        this.googleMap!!.addMarker(MarkerOptions().position(latLngOrigin).title("Ayala"))
        this.googleMap!!.addMarker(MarkerOptions().position(latLngDestination).title("SM City"))
        this.googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 14.5f))
    }
}