package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs4360app.adapters.ParkingLotAdapter
import com.example.cs4360app.databinding.ActivitySelectParkingLotBinding
import com.example.cs4360app.models.MSUDCampusLocation
import com.example.cs4360app.models.ParkingLot

@Suppress("DEPRECATION")
class SelectParkingLotActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectParkingLotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectParkingLotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create a list of ParkingLot objects with proximity values and addresses
        val parkingLots: List<ParkingLot> = listOf(
            ParkingLot(
                id = "1",
                name = "Jordan Parking Garage",
                cost = 10.0,
                rating = 4.2f,
                location = MSUDCampusLocation.JORDAN_PARKING_GARAGE,
                isMsudParkingLot = true,
                availabilityPercentage = 90,
                proximity = 100,
                address = "123 Jordan St"  // Added address
            ),
            ParkingLot(
                id = "2",
                name = "Tivoli Parking Lot",
                cost = 8.0,
                rating = 3.8f,
                location = MSUDCampusLocation.TIVOLI_PARKING_LOT,
                isMsudParkingLot = true,
                availabilityPercentage = 30,
                proximity = 200,
                address = "456 Tivoli St"  // Added address
            ),
            ParkingLot(
                id = "3",
                name = "Auraria West",
                cost = 5.0,
                rating = 4.0f,
                location = MSUDCampusLocation.AURARIA_WEST,
                isMsudParkingLot = true,
                availabilityPercentage = 75,
                proximity = 150,
                address = "789 Auraria W"  // Added address
            ),
            ParkingLot(
                id = "4",
                name = "9th and Walnut",
                cost = 6.0,
                rating = 4.5f,
                location = MSUDCampusLocation.NINTH_AND_WALNUT,
                isMsudParkingLot = true,
                availabilityPercentage = 10,
                proximity = 400,
                address = "654 Ninth St"  // Added address
            ),
            ParkingLot(
                id = "5",
                name = "Auraria East",
                cost = 9.0,
                rating = 3.9f,
                location = MSUDCampusLocation.AURARIA_EAST,
                isMsudParkingLot = true,
                availabilityPercentage = 50,
                proximity = 300,
                address = "321 Auraria E"  // Added address
            )
        )

        // Set up RecyclerView for displaying parking lots
        binding.parkingLotRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ParkingLotAdapter(parkingLots) { parkingLot ->
            // Check if a timer is already active
            val sharedPreferences = getSharedPreferences("timer_info", MODE_PRIVATE)
            val isTimerActive = sharedPreferences.getBoolean("TIMER_ACTIVE", false)
            if (isTimerActive) {
                // Show a message to the user
                Toast.makeText(this, "A timer is already running. Please wait until the current timer finishes.", Toast.LENGTH_LONG).show()
            }
            else {
                // Handle parking lot click and pass data to PayParkingLotActivity
                val intent = Intent(this, PayParkingLotActivity::class.java).apply {
                    putExtra("PARKING_LOT_ID", parkingLot.id)
                    putExtra("PARKING_LOT_NAME", parkingLot.name)
                    putExtra("PARKING_LOT_COST", parkingLot.cost)
                    putExtra("PARKING_LOT_RATING", parkingLot.rating)
                    putExtra("PARKING_LOT_PROXIMITY", parkingLot.proximity)
                    putExtra("PARKING_LOT_ADDRESS", parkingLot.address)
                }
                startActivity(intent) // Start PayParkingLotActivity
            }
        }
        binding.parkingLotRecyclerView.adapter = adapter

        // Optional: Handle the Back button click
        binding.backButton.setOnClickListener {
            onBackPressed() // Navigate to the previous activity
        }
    }
}