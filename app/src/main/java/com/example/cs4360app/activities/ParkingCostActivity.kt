package com.example.cs4360app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R

@Suppress("DEPRECATION")
class ParkingCostActivity : AppCompatActivity() {

    private lateinit var confirmButton: Button
    private lateinit var backButton: Button
    private lateinit var daysSelectedTextView: TextView
    private lateinit var parkingLotChosenTextView: TextView
    private lateinit var costPerDayTextView: TextView
    private lateinit var totalCostTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_cost)

        confirmButton = findViewById(R.id.confirm_button)
        backButton = findViewById(R.id.back_button)
        daysSelectedTextView = findViewById(R.id.days_selected_text_view)
        parkingLotChosenTextView = findViewById(R.id.parking_lot_chosen_text_view)
        costPerDayTextView = findViewById(R.id.cost_per_day_text_view)
        totalCostTextView = findViewById(R.id.total_cost_text_view)

        // Get the selected parking lot details from the intent extras
        val parkingLotCost = intent.getDoubleExtra("parking_lot_cost", 0.0)
        val numberOfDays = intent.getIntExtra("number_of_days", 1) // Get the number of days
        val parkingLotName = intent.getStringExtra("parking_lot_name") ?: "Unknown Parking Lot"

        // Display the information
        displayDaysSelected(numberOfDays)
        displayParkingLotChosen(parkingLotName)
        displayCostPerDay(parkingLotCost)

        // Calculate total cost for display
        val totalCost = parkingLotCost * numberOfDays
        updateTotalCost(totalCost)

        confirmButton.setOnClickListener {
            // After confirming, redirect to MapsActivity
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            onBackPressed() // Navigate back to the previous screen
        }
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun updateTotalCost(cost: Double) {
        // Update the TextView with the total cost
        totalCostTextView.text = "Total Cost: $${String.format("%.2f", cost)}"
    }

    @SuppressLint("SetTextI18n")
    private fun displayDaysSelected(days: Int) {
        // Update the TextView to display the number of days selected
        daysSelectedTextView.text = "Days Selected: $days"
    }

    @SuppressLint("SetTextI18n")
    private fun displayParkingLotChosen(parkingLotName: String) {
        // Update the TextView to display the chosen parking lot
        parkingLotChosenTextView.text = "Parking Lot Chosen: $parkingLotName"
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun displayCostPerDay(cost: Double) {
        // Update the TextView to display the cost per day
        costPerDayTextView.text = "Cost Per Day: $${String.format("%.2f", cost)}"
    }
}