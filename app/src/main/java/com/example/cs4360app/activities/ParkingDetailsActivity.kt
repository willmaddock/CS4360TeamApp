package com.example.cs4360app.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R
import com.example.cs4360app.managers.ParkingLotManager
import com.example.cs4360app.models.ParkingLot
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.*

@Suppress("DEPRECATION")
class ParkingDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var parkingListView: ListView
    private lateinit var nearbyParkingLots: List<ParkingLot>

    // Parking lots with their coordinates
    private val parkingLotCoordinates = mapOf(
        "Dogwood Parking Lot" to LatLng(39.74396, -105.00869),
        "Tivoli Parking Lot" to LatLng(39.7459, -105.00609),
        "Cherry Parking Lot" to LatLng(39.74378, -105.01021),
        "Spruce Parking Lot" to LatLng(39.74407, -105.00842),
        "Fir Parking Lot" to LatLng(39.74083, -105.00909),
        "Nutmeg Lot" to LatLng(39.74227, -105.00056),
        "Boulder Creek" to LatLng(39.740945, -105.003022),
        "Elm Parking Lot" to LatLng(39.74255, -105.0106),
        "7th Street Garage" to LatLng(39.74312, -105.00587),
        "5th Street Garage" to LatLng(39.744167, -105.009444),
        "Holly Lot" to LatLng(39.744722, -105.007222),
        "Juniper Lot" to LatLng(39.740833, -105.005833),
        "Maple Lot" to LatLng(39.743333, -105.008333),
        "Oak Lot" to LatLng(39.740556, -105.007222),
        "Aspen Lot" to LatLng(39.742222, -105.006944),
        "Walnut Lot" to LatLng(39.743888, -105.008333),
        "Beech Lot" to LatLng(39.740833, -105.005833),
        "Birch Lot" to LatLng(39.740000, -105.006000)
    )

    // Map to hold proximity distances
    private val proximityMap = mutableMapOf<String, Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_details)

        // Initialize map fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        parkingListView = findViewById(R.id.parkingListView)
        setVenueDetails()

        // Back button setup
        findViewById<Button>(R.id.backButton).setOnClickListener {
            onBackPressed()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val ballArenaLocation = LatLng(39.747397, -105.0079)
        mMap.addMarker(MarkerOptions().position(ballArenaLocation).title("Ball Arena - Prius West Lot"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ballArenaLocation, 15f))
        mMap.uiSettings.isZoomControlsEnabled = true

        nearbyParkingLots = ParkingLotManager.getNearbyParkingLots(ballArenaLocation)

        // Calculate proximity for each parking lot and store it
        nearbyParkingLots.forEach { lot ->
            val lotLocation = parkingLotCoordinates[lot.name] ?: return@forEach
            proximityMap[lot.name] = calculateProximity(ballArenaLocation, lotLocation)
        }

        ParkingLotManager.loadParkingLots(mMap)

        mMap.setOnMarkerClickListener { marker ->
            val selectedLot = nearbyParkingLots.find { it.name == marker.title }
            selectedLot?.let {
                val distanceFeet = proximityMap[it.name] ?: 0.0
                val distanceMiles = distanceFeet / 5280 // 1 mile = 5280 feet
                val distanceSteps = (distanceFeet / 2.5).toInt() // Assume an average step length of 2.5 feet

                parkingListView.adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    listOf(
                        "Name: ${it.name}",
                        "Cost: \$${it.cost}",
                        "Availability: ${it.availabilityPercentage}%",
                        "Proximity To Ball Arena: ${"%.2f".format(distanceFeet)} ft (${"%.2f".format(distanceMiles)} mi, $distanceSteps steps)",
                        "Address: ${it.address}"
                    )
                )
            }
            false
        }

        displayParkingLots(nearbyParkingLots)
    }

    @SuppressLint("SetTextI18n")
    private fun setVenueDetails() {
        findViewById<TextView>(R.id.venueNameTextView).text = "Ball Arena - Prius West Lot"
        findViewById<TextView>(R.id.venueAddressTextView).text = "1000 Chopper Circle"
        findViewById<TextView>(R.id.venueCityStateTextView).text = "Denver, Colorado 80204"
    }

    private fun displayParkingLots(parkingLots: List<ParkingLot>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, parkingLots.map { it.name })
        parkingListView.adapter = adapter
    }

    private fun calculateProximity(ballArena: LatLng, lot: LatLng): Double {
        // Haversine formula for distance between two points on Earth
        val earthRadiusFeet = 20925524.9 // Radius of the Earth in feet
        val dLat = Math.toRadians(lot.latitude - ballArena.latitude)
        val dLng = Math.toRadians(lot.longitude - ballArena.longitude)
        val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(ballArena.latitude)) *
                cos(Math.toRadians(lot.latitude)) * sin(dLng / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadiusFeet * c
    }
}