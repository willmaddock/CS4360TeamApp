package com.example.cs4360app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R

class PayParkingMeterActivity : AppCompatActivity() {

    private lateinit var licensePlateInput: EditText
    private lateinit var parkingDurationSeekBar: SeekBar
    private lateinit var selectedTimeText: TextView
    private lateinit var costPerHourText: TextView
    private lateinit var totalCostText: TextView
    private lateinit var payButton: Button
    private lateinit var backButton: Button

    private val ratePerMinute = 5.35f / 150 // $5.35 for 2 hours and 30 minutes (150 minutes)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_parking_meter)

        // Bind UI elements
        licensePlateInput = findViewById(R.id.license_plate_input)
        parkingDurationSeekBar = findViewById(R.id.parking_duration_seekbar)
        selectedTimeText = findViewById(R.id.selected_time_text)
        costPerHourText = findViewById(R.id.cost_per_hour_text)
        totalCostText = findViewById(R.id.total_cost_text)
        payButton = findViewById(R.id.pay_button)
        backButton = findViewById(R.id.back_button)

        // Set up SeekBar listener
        parkingDurationSeekBar.max = 240 // Maximum of 4 hours
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

        // Set up Pay button click listener
        payButton.setOnClickListener {
            val licensePlate = licensePlateInput.text.toString().trim()
            val timeInMinutes = parkingDurationSeekBar.progress

            // Validate inputs
            if (licensePlate.isEmpty()) {
                Toast.makeText(this, "Please enter a license plate.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (timeInMinutes < 15) {
                Toast.makeText(this, "Parking duration must be at least 15 minutes.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simulate successful payment
            val amountPaid = calculatePayment(timeInMinutes)
            onPaymentSuccess(amountPaid, timeInMinutes)
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

    private fun onPaymentSuccess(amountPaid: Float, timeInMinutes: Int) {
        // Store payment status in SharedPreferences
        val sharedPreferences = getSharedPreferences("payment_prefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("payment_active", true)
            putLong("remaining_time", timeInMinutes.toLong() * 60) // Store remaining time in seconds
            apply()
        }

        // Redirect to TimerActivity
        val intent = Intent(this, TimerActivity::class.java)
        intent.putExtra("amountPaid", amountPaid)
        intent.putExtra("timeInMinutes", timeInMinutes.toLong())
        startActivity(intent)
        finish() // Close this activity
    }

    // Calculate the payment based on the time (using rate per minute)
    private fun calculatePayment(minutes: Int): Float {
        return minutes * ratePerMinute
    }
}