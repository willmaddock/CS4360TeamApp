package com.example.cs4360app.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cs4360app.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val zoomIncrement = 1.0f
    private var currentZoomLevel: Float = 15f

    private val MIN_ZOOM_LEVEL = 5.0f
    private val MAX_ZOOM_LEVEL = 20.0f
    private val LOCATION_REQUEST_CODE = 1000

    private val GARAGE_LOCATION = LatLng(39.7392, -104.9903) // Coordinates for 7th Street Garage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Set up zoom in and zoom out buttons
        findViewById<Button>(R.id.zoom_in_button).setOnClickListener {
            zoomIn()
        }

        findViewById<Button>(R.id.zoom_out_button).setOnClickListener {
            zoomOut()
        }

        checkLocationPermission()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Enable the 'My Location' button if the permission is granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }

        // Set the map's default location to 7th Street Garage
        mMap.addMarker(MarkerOptions().position(GARAGE_LOCATION).title("7th Street Garage"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GARAGE_LOCATION, currentZoomLevel))
    }

    private fun zoomIn() {
        if (::mMap.isInitialized) {
            if (currentZoomLevel < MAX_ZOOM_LEVEL) {
                currentZoomLevel += zoomIncrement
                mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel))
            }
        }
    }

    private fun zoomOut() {
        if (::mMap.isInitialized) {
            if (currentZoomLevel > MIN_ZOOM_LEVEL) {
                currentZoomLevel -= zoomIncrement
                mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel))
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (::mMap.isInitialized) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    mMap.isMyLocationEnabled = true
                }
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}