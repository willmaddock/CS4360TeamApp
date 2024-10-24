package com.example.cs4360app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R

class PayParkingMeterActivity : AppCompatActivity() {

    private lateinit var parkingDurationSeekBar: SeekBar
    private lateinit var selectedTimeText: TextView
    private lateinit var totalCostText: TextView
    private lateinit var startBudgetSimulatorButton: Button
    private lateinit var backButton: Button

    private val commonCost = 5.35f // Fixed cost for 2 hours and 30 minutes (150 minutes)
    private val maxMinutes = 240 // Maximum parking time in minutes (4 hours)
    private val baseMinutes = 150 // Base time (2 hours 30 minutes)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_parking_meter)

        // Bind UI elements
        parkingDurationSeekBar = findViewById(R.id.parking_duration_seekbar)
        selectedTimeText = findViewById(R.id.selected_time_text)
        totalCostText = findViewById(R.id.total_cost_text)
        startBudgetSimulatorButton = findViewById(R.id.start_budget_simulator_button)
        backButton = findViewById(R.id.back_button)

        // Set up SeekBar listener
        parkingDurationSeekBar.max = maxMinutes // Maximum of 4 hours (240 minutes)
        parkingDurationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val hours = progress / 60
                val minutes = progress % 60
                selectedTimeText.text = "Selected Time: ${hours}h ${minutes}m"
                updateTotalCost(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Set up Start Budget Simulator button click listener
        startBudgetSimulatorButton.setOnClickListener {
            val timeInMinutes = parkingDurationSeekBar.progress

            // Validate inputs
            if (timeInMinutes < 15) {
                Toast.makeText(this,
                    getString(R.string.parking_duration_must_be_at_least_15_minutes), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Calculate payment and save end time in SharedPreferences
            val amountPaid = calculatePayment(timeInMinutes)
            onSimulationStart(amountPaid, timeInMinutes)
        }

        // Set up Back button click listener
        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun updateTotalCost(minutes: Int) {
        val totalCost = calculatePayment(minutes)
        totalCostText.text = "Total cost: $${String.format("%.2f", totalCost)}"
    }

    @SuppressLint("DefaultLocale", "StringFormatMatches")
    private fun onSimulationStart(amountPaid: Float, timeInMinutes: Int) {
        // Save end time in SharedPreferences
        val endTime = System.currentTimeMillis() + (timeInMinutes * 60 * 1000) // Calculate end time
        val sharedPreferences = getSharedPreferences("payment_prefs", MODE_PRIVATE)
        sharedPreferences.edit().putLong("end_time", endTime).apply()

        Toast.makeText(this,
            getString(
                R.string.starting_budget_simulation_for_over_minutes,
                String.format("%.2f", amountPaid),
                timeInMinutes
            ), Toast.LENGTH_SHORT).show()

        // Redirect to TimerActivity
        val intent = Intent(this, TimerActivity::class.java)
        startActivity(intent)
        finish() // Optionally finish this activity
    }

    private fun calculatePayment(minutes: Int): Float {
        return if (minutes <= baseMinutes) {
            commonCost // If the time is within or equal to 2 hours and 30 minutes, charge $5.35
        } else {
            // Additional cost for any time beyond the base time (2 hours 30 minutes)
            val extraMinutes = minutes - baseMinutes
            commonCost + (extraMinutes * (commonCost / baseMinutes)) // Pro-rated extra time
        }
    }
}