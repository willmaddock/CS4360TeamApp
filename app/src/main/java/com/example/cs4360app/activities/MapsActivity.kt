package com.example.cs4360app.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cs4360app.R
import com.example.cs4360app.models.MSUDCampusLocation
import com.example.cs4360app.models.ParkingLot
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
    private val GARAGE_LOCATION = LatLng(39.745209, -105.005908) // Corrected coordinates for 7th Street Garage
    private var costFilterEnabled = false
    private var maxCost: Float = 10.0f // Default to 10 dollars

    private val parkingLots = listOf(
        ParkingLot("1", "Jordan Parking Garage", 10.0, 4.5f, MSUDCampusLocation.JORDAN_PARKING_GARAGE, true),
        ParkingLot("2", "Tivoli Parking Lot", 8.0, 4.0f, MSUDCampusLocation.TIVOLI_PARKING_LOT, true),
        ParkingLot("3", "Auraria West", 5.0, 3.5f, MSUDCampusLocation.AURARIA_WEST, true),
        ParkingLot("4", "Auraria East", 7.0, 3.0f, MSUDCampusLocation.AURARIA_EAST, true),
        ParkingLot("5", "Ninth and Walnut", 6.0, 2.5f, MSUDCampusLocation.NINTH_AND_WALNUT, true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<Button>(R.id.zoom_in_button).setOnClickListener { zoomIn() }
        findViewById<Button>(R.id.zoom_out_button).setOnClickListener { zoomOut() }

        val costFilterToggle = findViewById<ToggleButton>(R.id.cost_filter_toggle)
        costFilterToggle.setOnCheckedChangeListener { _, isChecked ->
            costFilterEnabled = isChecked
            updateMapWithFilter()
        }

        checkLocationPermission()

        // Retrieve max cost from intent, default to 10 if not provided
        maxCost = intent.getFloatExtra("maxCost", 10.0f)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }

        mMap.addMarker(MarkerOptions().position(GARAGE_LOCATION).title("7th Street Garage"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GARAGE_LOCATION, currentZoomLevel))

        updateMapWithFilter()
    }

    private fun zoomIn() {
        if (::mMap.isInitialized && currentZoomLevel < MAX_ZOOM_LEVEL) {
            currentZoomLevel += zoomIncrement
            mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel))
        }
    }

    private fun zoomOut() {
        if (::mMap.isInitialized && currentZoomLevel > MIN_ZOOM_LEVEL) {
            currentZoomLevel -= zoomIncrement
            mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel))
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (::mMap.isInitialized) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                mMap.isMyLocationEnabled = true
            }
            Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateMapWithFilter() {
        mMap.clear()
        if (costFilterEnabled) {
            parkingLots.filter { it.cost <= maxCost }
                .forEach { parkingLot ->
                    val location = parkingLot.location?.let { getLatLngForLocation(it) }
                    location?.let {
                        val markerOptions = MarkerOptions()
                            .position(it)
                            .title(parkingLot.name)
                            .snippet("Cost: $${parkingLot.cost}, Rating: ${parkingLot.rating}, Address: ${getAddressForLocation(parkingLot.location)}")
                        mMap.addMarker(markerOptions)
                    }
                }
        } else {
            parkingLots.forEach { parkingLot ->
                val location = parkingLot.location?.let { getLatLngForLocation(it) }
                location?.let {
                    val markerOptions = MarkerOptions()
                        .position(it)
                        .title(parkingLot.name)
                        .snippet("Cost: $${parkingLot.cost}, Rating: ${parkingLot.rating}, Address: ${getAddressForLocation(parkingLot.location)}")
                    mMap.addMarker(markerOptions)
                }
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GARAGE_LOCATION, currentZoomLevel))
    }

    private fun getLatLngForLocation(location: MSUDCampusLocation): LatLng {
        return when (location) {
            MSUDCampusLocation.JORDAN_PARKING_GARAGE -> LatLng(39.745473, -105.007460)
            MSUDCampusLocation.TIVOLI_PARKING_LOT -> LatLng(39.744338, -105.002847)
            MSUDCampusLocation.AURARIA_WEST -> LatLng(39.744556, -105.009145)
            MSUDCampusLocation.AURARIA_EAST -> LatLng(39.743115, -105.000376)
            MSUDCampusLocation.NINTH_AND_WALNUT -> LatLng(39.743883, -105.001878)
        }
    }

    private fun getAddressForLocation(location: MSUDCampusLocation): String {
        return when (location) {
            MSUDCampusLocation.JORDAN_PARKING_GARAGE -> "1001 10th St, Denver, CO 80204"
            MSUDCampusLocation.TIVOLI_PARKING_LOT -> "900 Auraria Pkwy, Denver, CO 80204"
            MSUDCampusLocation.AURARIA_WEST -> "1100 9th St, Denver, CO 80204"
            MSUDCampusLocation.AURARIA_EAST -> "1100 7th St, Denver, CO 80204"
            MSUDCampusLocation.NINTH_AND_WALNUT -> "9th St & Walnut St, Denver, CO 80204"
        }
    }
}