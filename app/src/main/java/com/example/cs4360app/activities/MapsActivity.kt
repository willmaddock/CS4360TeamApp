package com.example.cs4360app.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.cs4360app.R
import com.example.cs4360app.managers.DrawerManager
import com.example.cs4360app.managers.FilterManager
import com.example.cs4360app.managers.ParkingLotManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var infoIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Initialize SharedPreferences for filters
        sharedPreferences = getSharedPreferences("filter_prefs", MODE_PRIVATE)

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        // Set up the Floating Action Button to open the drawer
        findViewById<FloatingActionButton>(R.id.open_drawer_fab).setOnClickListener {
            drawerLayout.open()
        }

        // Initialize the map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize the info icon and set its click listener
        infoIcon = findViewById(R.id.info_icon)
        infoIcon.setOnClickListener {
            showInfoDialog()
        }

        // Set up zoom buttons
        setupZoomButtons()

        // Delegate drawer and filter setup to managers
        DrawerManager.setupDrawer(this, navigationView)  // Managing drawer setup via DrawerManager
        findViewById<Button>(R.id.filter_button).setOnClickListener {
            FilterManager.showFilterDialog(this, mMap)  // Handling filters using FilterManager
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Set the camera to the Jordan Parking Garage coordinates
        val dogWoodParkingLot = LatLng(39.74396, -105.00869)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dogWoodParkingLot, 15f))  // Initial zoom level

        // Load parking lots (filters can be passed via FilterManager if needed)
        ParkingLotManager.loadParkingLots(mMap)
    }

    private fun setupZoomButtons() {
        findViewById<Button>(R.id.zoom_in_button).setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())
        }
        findViewById<Button>(R.id.zoom_out_button).setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }
    }

    private fun showInfoDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Parking Lot Availability")
            .setMessage(
                """
                The colored markers represent the availability of parking lots:
                - Green: Available
                - Yellow: Almost Full
                - Red: Full
                """.trimIndent()
            )
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}