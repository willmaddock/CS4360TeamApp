package com.example.cs4360app.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.ToggleButton
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
    private var costFilterEnabled = false
    private var maxCost = -1.0

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

        // Set up cost filter toggle button
        val costFilterToggle = findViewById<ToggleButton>(R.id.cost_filter_toggle)
        costFilterToggle.setOnCheckedChangeListener { _, isChecked ->
            costFilterEnabled = isChecked
            if (costFilterEnabled) {
                applyCostFilter(maxCost)
            } else {
                showAllParkingLots()
            }
        }

        checkLocationPermission()

        // Retrieve max cost from intent if provided
        maxCost = intent.getDoubleExtra("maxCost", -1.0)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Enable the 'My Location' button if the permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }

        // Set the map's default location to 7th Street Garage
        mMap.addMarker(MarkerOptions().position(GARAGE_LOCATION).title("7th Street Garage"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GARAGE_LOCATION, currentZoomLevel))

        // Apply the cost filter if it was enabled previously
        if (costFilterEnabled && maxCost != -1.0) {
            applyCostFilter(maxCost)
        }
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
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

    private fun applyCostFilter(maxCost: Double) {
        // Implement filtering logic based on maxCost
        // Example placeholder function
        Toast.makeText(this, "Applying cost filter with max cost: $$maxCost", Toast.LENGTH_SHORT).show()
        // Update map markers based on filtered parking lots
    }

    private fun showAllParkingLots() {
        // Implement logic to show all parking lots without any filter
        Toast.makeText(this, "Showing all parking lots", Toast.LENGTH_SHORT).show()
        // Update map markers to show all parking lots
    }
}