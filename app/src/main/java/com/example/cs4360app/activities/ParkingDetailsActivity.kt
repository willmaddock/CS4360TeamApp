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
        "Dogwood Parking Lot" to LatLng(39.744562552494635, -105.0083253825282),
        "Tivoli Parking Lot" to LatLng(39.74641815233135, -105.0061195427284),
        "Cherry Parking Lot" to LatLng(39.74411512001208, -105.00958392912607),
        "Spruce Parking Lot" to LatLng(39.74461804779121, -105.00698112842873),
        "Fir Parking Lot" to LatLng(39.74119220167536, -105.0087527236546),
        "Nutmeg Lot" to LatLng(39.74226310246321, -105.00090275359969),
        "Boulder Creek" to LatLng(39.74118605149313, -105.00285485547928),
        "Elm Parking Lot" to LatLng(39.742554638969565, -105.00940650343364),
        "7th Street Garage" to LatLng(39.7434473946466, -105.00752457926319),
        "5th Street Garage" to LatLng(39.74301082876547, -105.01033151701921),
        "Holly Lot" to LatLng(39.74220914411681, -105.00579575657247),
        "Juniper Lot" to LatLng(39.74077187408226, -105.00622955600369),
        "Maple Lot" to LatLng(39.74191482504014, -105.00057102460512),
        "Oak Lot" to LatLng(39.74239064023424, -105.0012472414017),
        "Aspen Lot" to LatLng(39.742527121045995, -105.01097830588711),
        "Walnut Lot" to LatLng(39.74245354173831, -105.01186504301093),
        "Beech Lot" to LatLng(39.74101837825164, -105.01001395345327),
        "Birch Lot" to LatLng(39.74101837825164, -105.01001395345327)
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