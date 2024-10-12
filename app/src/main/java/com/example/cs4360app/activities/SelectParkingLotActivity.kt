package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
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

        // Sample parking lots data
        val parkingLots = listOf(
            ParkingLot("1", "Dogwood Parking Lot", 7.25, 4.5f, MSUDCampusLocation.DOGWOOD_PARKING_LOT, true, 90, 100, "7th St & Walnut Auraria Denver, CO 80204"),
            ParkingLot("2", "Tivoli Parking Lot", 7.25, 4.0f, MSUDCampusLocation.TIVOLI_PARKING_LOT, true, 30, 200, "901 Walnut St Auraria Denver, CO 80204"),
            ParkingLot("3", "Cherry Parking Lot", 5.75, 3.5f, MSUDCampusLocation.CHERRY_PARKING_LOT, true, 75, 150, "605 Walnut St Auraria Denver, CO 80204"),
            ParkingLot("4", "Spruce Parking Lot", 7.25, 3.0f, MSUDCampusLocation.SPRUCE_PARKING_LOT, true, 50, 300, "800 Walnut St Auraria Denver, CO 80204"),
            ParkingLot("5", "Fir Parking Lot", 5.75, 2.5f, MSUDCampusLocation.FIR_PARKING_LOT, true, 10, 400, "555 Curtis St Auraria Denver, CO 80204"),
            ParkingLot("6", "Nutmeg Lot", 7.25, 4.0f, MSUDCampusLocation.NUTMEG_LOT, true, 70, 250, "1155 St Francis Way Northwestern Denver, CO 80204"),
            ParkingLot("7", "Boulder Creek", 2.00, 3.8f, MSUDCampusLocation.BOULDER_CREEK, true, 40, 120, "900 10th St Plaza Auraria Denver, CO 80204"),
            ParkingLot("8", "Elm Parking Lot", 5.75, 3.9f, MSUDCampusLocation.ELM_PARKING_LOT, true, 55, 180, "1301 5th St Auraria Denver, CO 80204"),
            ParkingLot("9", "7th Street Garage", 7.25, 4.1f, MSUDCampusLocation.SEVENTH_LOT, true, 65, 220, "777 Lawrence Way Auraria Denver, CO 80204")
        )

        // Set up RecyclerView for displaying parking lots
        binding.parkingLotRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ParkingLotAdapter(parkingLots) { parkingLot ->
            // Handle parking lot click
            val intent = Intent(this, PayParkingMeterActivity::class.java)
            intent.putExtra("selected_parking_lot", parkingLot) // Pass selected parking lot
            startActivity(intent)
        }
        binding.parkingLotRecyclerView.adapter = adapter

        // Handle the Back button click
        binding.backButton.setOnClickListener {
            onBackPressed() // Navigate to the previous activity
        }
    }
}