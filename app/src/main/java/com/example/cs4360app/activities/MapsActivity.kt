package com.example.cs4360app.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.cs4360app.MainActivity
import com.example.cs4360app.R
import com.example.cs4360app.models.MSUDCampusLocation
import com.example.cs4360app.models.ParkingLot
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private val zoomIncrement = 1.0f
    private var currentZoomLevel: Float = 15f
    private val MIN_ZOOM_LEVEL = 5.0f
    private val MAX_ZOOM_LEVEL = 20.0f
    private val LOCATION_REQUEST_CODE = 1000
    private val GARAGE_LOCATION = LatLng(39.745209, -105.005908) // Corrected coordinates for 7th Street Garage
    private var costFilterEnabled = false
    private var maxCost: Float = 10.0f // Default to 10 dollars

    // List of parking lots
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

        // Set up the drawer layout and navigation view
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Modify drawer menu items based on login state
        val menu = navigationView.menu
        val loginMenuItem = menu.findItem(R.id.nav_login)
        val logoutMenuItem = menu.findItem(R.id.nav_logout)
        val guestMenuItem = menu.findItem(R.id.nav_guest)
        val backToMenuItem = menu.findItem(R.id.nav_back_to_menu)
        val chatMenuItem = menu.findItem(R.id.nav_chat) // Get the Chat menu item
        val notificationsMenuItem = menu.findItem(R.id.nav_notifications) // Get the Notifications menu item

        // Check if the user is logged in or a guest
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        // Check if user is logged in
        if (currentUser != null) {
            // User is logged in
            loginMenuItem.isVisible = false
            logoutMenuItem.isVisible = true
            guestMenuItem.isVisible = false // Hide guest menu item for logged in users
            backToMenuItem.isVisible = true // Show back to menu item for logged in users
            chatMenuItem.isVisible = true // Show chat menu item for logged in users
            notificationsMenuItem.isVisible = true // Show notifications menu item for logged in users
        } else {
            // User is logged out
            loginMenuItem.isVisible = true
            logoutMenuItem.isVisible = false
            guestMenuItem.isVisible = false // Hide guest menu item for logged out users
            backToMenuItem.isVisible = false // Hide back to menu item for logged out users
            chatMenuItem.isVisible = false // Hide chat menu item for guests
            notificationsMenuItem.isVisible = false // Hide notifications menu item for guests
        }

        // Handle drawer navigation item selection
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_login -> {
                    showLoginActivity()
                    true
                }
                R.id.nav_logout -> {
                    logoutUser()
                    true
                }
                R.id.nav_guest -> {
                    continueAsGuest()
                    true
                }
                R.id.nav_back_to_menu -> {
                    navigateToMenu()
                    true
                }
                R.id.nav_pay_parking_meter -> {
                    openPayParkingMeterActivity()
                    true
                }
                R.id.nav_payment -> {
                    openPaymentActivity()
                    true
                }
                R.id.nav_chat -> {
                    val intent = Intent(this, ChatActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_notifications -> {
                    val intent = Intent(this, NotificationsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Set up the map fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Set up zoom buttons
        findViewById<Button>(R.id.zoom_in_button).setOnClickListener { zoomIn() }
        findViewById<Button>(R.id.zoom_out_button).setOnClickListener { zoomOut() }

        // Set up cost filter toggle
        val costFilterToggle = findViewById<ToggleButton>(R.id.cost_filter_toggle)
        costFilterToggle.setOnCheckedChangeListener { _, isChecked ->
            costFilterEnabled = isChecked
            updateMapWithFilter()
        }

        // Set up Floating Action Button to open drawer
        val openDrawerFab = findViewById<FloatingActionButton>(R.id.open_drawer_fab)
        openDrawerFab.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }


        checkLocationPermission()

        // Retrieve max cost from intent, default to 10 if not provided
        maxCost = intent.getFloatExtra("maxCost", 10.0f)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Enable location if permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }

        // Add marker for the garage location and move the camera
        mMap.addMarker(MarkerOptions().position(GARAGE_LOCATION).title("7th Street Garage"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GARAGE_LOCATION, currentZoomLevel))

        // Update map with parking lot filters
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
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.isMyLocationEnabled = true
                }
            }
            Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateMapWithFilter() {
        mMap.clear() // Clear previous markers
        val filteredParkingLots = if (costFilterEnabled) {
            parkingLots.filter { it.cost <= maxCost } // Filter based on cost
        } else {
            parkingLots // No filter applied
        }

        filteredParkingLots.forEach { parkingLot ->
            val location = parkingLot.location?.let { getLatLngForLocation(it) }
            location?.let {
                val markerOptions = MarkerOptions()
                    .position(it)
                    .title("${parkingLot.name} - $${parkingLot.cost}")
                mMap.addMarker(markerOptions)
            }
        }
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

    private fun showLoginActivity() {
        // Implement login activity logic here
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun logoutUser() {
        // Implement logout logic
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        recreate() // Refresh the activity to update UI
    }

    private fun continueAsGuest() {
        // Implement guest continuation logic
        Toast.makeText(this, "Continuing as Guest", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToMenu() {
        // Implement navigation to the main menu logic
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun openPayParkingMeterActivity() {
        // Implement logic to open Pay Parking Meter activity
        val intent = Intent(this, PaymentActivity::class.java)
        startActivity(intent)
    }

    private fun openPaymentActivity() {
        // Implement logic to open Payment activity
        val intent = Intent(this, PaymentActivity::class.java)
        startActivity(intent)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}