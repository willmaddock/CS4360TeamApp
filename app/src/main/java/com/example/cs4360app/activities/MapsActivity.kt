package com.example.cs4360app.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.cs4360app.MainActivity
import com.example.cs4360app.R
import com.example.cs4360app.managers.ParkingLotManager
import com.example.cs4360app.models.MSUDCampusLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var maxCost: Double = 10.0 // Default max cost
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var costFilterToggle: ToggleButton
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("payment_prefs", MODE_PRIVATE)

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        // Setup the navigation drawer
        setupDrawer()

        // Get max cost from intent, if not available, default to 10.0
        maxCost = intent.getDoubleExtra("maxCost", 10.0)

        // Initialize the map
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Setup zoom buttons for map
        setupZoomButtons()

        // Initialize cost filter toggle
        costFilterToggle = findViewById(R.id.cost_filter_toggle)
        costFilterToggle.setOnCheckedChangeListener { _, isChecked ->
            updateMapWithFilter(isChecked) // Refresh map markers based on the toggle state
        }

        // Floating Action Button to open the drawer
        findViewById<FloatingActionButton>(R.id.open_drawer_fab).setOnClickListener {
            drawerLayout.open()
        }

        // Update the drawer menu initially
        updateDrawerMenu()
    }

    private fun setupDrawer() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_login -> {
                    startActivityForResult(Intent(this, LoginActivity::class.java), 1)
                    true
                }
                R.id.nav_logout -> {
                    FirebaseAuth.getInstance().signOut() // Sign out the user
                    updateMenuForUser() // Update menu items after logout
                    true
                }
                R.id.nav_back_to_menu -> {
                    startActivityForResult(Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    }, 1)
                    true
                }
                R.id.nav_payment -> {
                    startActivityForResult(Intent(this, PaymentActivity::class.java), 1)
                    true
                }
                R.id.nav_chat -> {
                    startActivityForResult(Intent(this, ChatActivity::class.java), 1)
                    true
                }
                R.id.nav_notifications -> {
                    startActivityForResult(Intent(this, NotificationsActivity::class.java), 1)
                    true
                }
                R.id.nav_timer -> {
                    startActivityForResult(Intent(this, TimerActivity::class.java), 1)
                    true
                }
                R.id.nav_submit_review -> {
                    startActivityForResult(Intent(this, SubmitReviewActivity::class.java), 1)
                    true
                }
                R.id.nav_submit_petition -> {
                    startActivityForResult(Intent(this, PetitionActivity::class.java), 1)
                    true
                }
                R.id.nav_take_survey -> {
                    // Start the SurveyActivity for guest users
                    if (!isLoggedIn()) {
                        startActivityForResult(Intent(this, SurveyActivity::class.java), 1)
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }
        updateMenuForUser() // Update menu items on creation based on user status
    }

    private fun setupZoomButtons() {
        findViewById<Button>(R.id.zoom_in_button).setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())
        }

        findViewById<Button>(R.id.zoom_out_button).setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }
    }

    private fun updateMenuForUser() {
        val menu = navigationView.menu
        val isLoggedIn = auth.currentUser != null

        // Show/hide menu items based on user status
        menu.findItem(R.id.nav_login).isVisible = !isLoggedIn
        menu.findItem(R.id.nav_logout).isVisible = isLoggedIn
        menu.findItem(R.id.nav_back_to_menu).isVisible = isLoggedIn
        menu.findItem(R.id.nav_chat).isVisible = isLoggedIn
        menu.findItem(R.id.nav_notifications).isVisible = isLoggedIn
        menu.findItem(R.id.nav_submit_petition).isVisible = isLoggedIn
        menu.findItem(R.id.nav_submit_review).isVisible = isLoggedIn
        menu.findItem(R.id.nav_take_survey).isVisible = !isLoggedIn // Show for guests only

        // Update visibility of the Timer option based on active payment
        updateDrawerMenu()
    }

    // Method to check if payment is active and update Timer visibility
    private fun updateDrawerMenu() {
        val isPaymentActive = isTimerActive() // Check payment active status
        val menu = navigationView.menu
        val timerMenuItem = menu.findItem(R.id.nav_timer) // Find the Timer menu item

        // Show or hide the Timer item based on payment status
        timerMenuItem.isVisible = isPaymentActive
    }

    private fun isTimerActive(): Boolean {
        return sharedPreferences.getBoolean("payment_active", false)
    }

    private fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Move camera to the Jordan Parking Garage location
        val jordanParkingGarageLocation = ParkingLotManager.getLatLngForLocation(MSUDCampusLocation.JORDAN_PARKING_GARAGE)
        jordanParkingGarageLocation?.let {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15f)) // Set zoom level to 15
        }

        // Load parking lots from your data source
        ParkingLotManager.loadParkingLots(mMap, maxCost)
    }

    private fun updateMapWithFilter(isChecked: Boolean) {
        if (isChecked) {
            // Show only parking lots within a certain cost
            ParkingLotManager.loadParkingLots(mMap, maxCost)
        } else {
            // Show all parking lots
            ParkingLotManager.loadParkingLots(mMap, Double.MAX_VALUE) // Use a large number to show all
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Reopen the drawer when returning to this activity
        drawerLayout.open()
    }

    override fun onResume() {
        super.onResume()
        // Ensure the drawer is open if returning from another activity
        drawerLayout.open()
    }
}