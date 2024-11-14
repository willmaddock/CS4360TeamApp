package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs4360app.R
import com.example.cs4360app.adapters.ParkingLotAdapter
import com.example.cs4360app.databinding.ActivitySelectParkingLotBinding
import com.example.cs4360app.models.MSUDCampusLocation
import com.example.cs4360app.models.ParkingLot
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("DEPRECATION")
class SelectParkingLotActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectParkingLotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectParkingLotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sample parking lots data
        val parkingLots = listOf(
            ParkingLot("1", "Dogwood Parking Lot", 8.00, 4.5f, MSUDCampusLocation.DOGWOOD_PARKING_LOT, true, 90, 100, "7th St & Walnut Auraria Denver, CO 80204"),
            ParkingLot("2", "Tivoli Parking Garage", 8.00, 4.0f, MSUDCampusLocation.TIVOLI_PARKING_GARAGE, true, 30, 200, "901 Walnut St Auraria Denver, CO 80204"),
            ParkingLot("3", "Cherry Parking Lot", 6.50, 3.5f, MSUDCampusLocation.CHERRY_PARKING_LOT, true, 75, 150, "605 Walnut St Auraria Denver, CO 80204"),
            ParkingLot("4", "Spruce Parking Lot", 8.00, 3.0f, MSUDCampusLocation.SPRUCE_PARKING_LOT, true, 50, 300, "800 Walnut St Auraria Denver, CO 80204"),
            ParkingLot("5", "Fir Parking Lot", 6.50, 2.5f, MSUDCampusLocation.FIR_PARKING_LOT, true, 10, 400, "555 Curtis St Auraria Denver, CO 80204"),
            ParkingLot("6", "Nutmeg Lot", 8.00, 4.0f, MSUDCampusLocation.NUTMEG_LOT, true, 70, 250, "1155 St Francis Way Northwestern Denver, CO 80204"),
            ParkingLot("7", "Boulder Creek", 2.00, 3.8f, MSUDCampusLocation.BOULDER_CREEK, true, 40, 120, "900 10th St Plaza Auraria Denver, CO 80204"),
            ParkingLot("8", "Elm Parking Lot", 6.50, 3.9f, MSUDCampusLocation.ELM_PARKING_LOT, true, 55, 180, "1301 5th St Auraria Denver, CO 80204"),
            ParkingLot("9", "7th Street Garage", 8.00, 4.1f, MSUDCampusLocation.SEVENTH_LOT, true, 65, 220, "777 Lawrence Way Auraria Denver, CO 80204"),
            ParkingLot("10", "5th Street Garage", 6.50, 4.1f, MSUDCampusLocation.FIFTH_STREET_GARAGE, true, 65, 220, "5th St Garage Auraria Denver, CO 80204"),
            ParkingLot("11", "Holly Lot", 8.00, 3.9f, MSUDCampusLocation.HOLLY_LOT, true, 70, 180, "Holly Lot Auraria Denver, CO 80204"),
            ParkingLot("12", "Juniper Lot", 6.50, 3.8f, MSUDCampusLocation.JUNIPER_LOT, true, 75, 150, "Juniper Lot Auraria Denver, CO 80204"),
            ParkingLot("13", "Maple Lot", 8.00, 3.7f, MSUDCampusLocation.MAPLE_LOT, true, 80, 140, "Maple Lot Auraria Denver, CO 80204"),
            ParkingLot("14", "Oak Lot", 8.00, 3.6f, MSUDCampusLocation.OAK_LOT, true, 60, 130, "Oak Lot Auraria Denver, CO 80204"),
            ParkingLot("15", "Aspen Lot", 5.00, 4.0f, MSUDCampusLocation.ASPEN_LOT, true, 85, 240, "Aspen Lot Auraria Denver, CO 80204"),
            ParkingLot("16", "Walnut Lot", 5.00, 4.2f, MSUDCampusLocation.WALNUT_LOT, true, 40, 160, "Walnut Lot Auraria Denver, CO 80204"),
            ParkingLot("17", "Beech Lot", 5.00, 3.5f, MSUDCampusLocation.BEECH_LOT, true, 50, 190, "Beech Lot Auraria Denver, CO 80204"),
            ParkingLot("18", "Birch Lot", 5.00, 3.6f, MSUDCampusLocation.BIRCH_LOT, true, 55, 170, "Birch Lot Auraria Denver, CO 80204")
        )

        // Set up RecyclerView for displaying parking lots
        binding.parkingLotRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ParkingLotAdapter(parkingLots) { parkingLot ->
            // Handle parking lot click
            Toast.makeText(this, getString(R.string.selected, parkingLot.name), Toast.LENGTH_SHORT).show()

            // Get budget type and start/end dates from Intent extras
            val budgetType = intent.getStringExtra("selected_budget_type") // Corrected key
            var numberOfDays = intent.getIntExtra("number_of_days", 1) // Default to 1 if not provided

            // Calculate the number of days based on the semester option
            if (budgetType == "Semester") { // Check against the correct string
                // Retrieve start and end dates from Intent extras
                val startDateStr = intent.getStringExtra("start_date")
                val endDateStr = intent.getStringExtra("end_date")

                // Check if start or end dates are null
                if (startDateStr == null || endDateStr == null) {
                    Toast.makeText(this,
                        getString(R.string.start_and_end_dates_are_required_for_semester_budget), Toast.LENGTH_SHORT).show()
                    return@ParkingLotAdapter // Exit the lambda if dates are not provided
                }

                // Parse the dates
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val startDate = dateFormat.parse(startDateStr)
                val endDate = dateFormat.parse(endDateStr)

                // Calculate the number of days (Add 1 to count the starting date)
                if (startDate != null && endDate != null && endDate.time >= startDate.time) {
                    numberOfDays = ((endDate.time - startDate.time) / (1000 * 60 * 60 * 24)).toInt() + 1
                } else {
                    Toast.makeText(this, getString(R.string.invalid_date_range), Toast.LENGTH_SHORT).show()
                    return@ParkingLotAdapter
                }
            }

            // Calculate total cost
            val totalCost = numberOfDays * parkingLot.cost

            // Redirect to ParkingCostActivity and pass the selected parking lot details
            val intent = Intent(this, ParkingCostActivity::class.java).apply {
                putExtra("parking_lot_cost", parkingLot.cost) // Pass the cost of the selected lot
                putExtra("number_of_days", numberOfDays) // Pass the number of days
                putExtra("total_cost", totalCost) // Pass the total cost
                putExtra("parking_lot_name", parkingLot.name) // Pass the name of the selected parking lot
            }
            startActivity(intent)
        }
        binding.parkingLotRecyclerView.adapter = adapter

        // Handle the Back button click
        binding.backButton.setOnClickListener {
            onBackPressed() // Navigate to the previous activity
        }
    }
}