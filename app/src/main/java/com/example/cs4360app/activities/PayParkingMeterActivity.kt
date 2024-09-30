package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R

class PayParkingMeterActivity : AppCompatActivity() {

    private lateinit var licensePlateInput: EditText
    private lateinit var timeInput: EditText
    private lateinit var payButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_parking_meter)

        // Bind UI elements
        licensePlateInput = findViewById(R.id.license_plate_input)
        timeInput = findViewById(R.id.time_input)
        payButton = findViewById(R.id.pay_button)
        backButton = findViewById(R.id.back_button)

        // Set up Pay button click listener
        payButton.setOnClickListener {
            val licensePlate = licensePlateInput.text.toString().trim()
            val timeInMinutes = timeInput.text.toString().trim()

            // Validate inputs
            if (licensePlate.isEmpty()) {
                Toast.makeText(this, "Please enter a license plate.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (timeInMinutes.isEmpty() || timeInMinutes.toIntOrNull() == null || timeInMinutes.toInt() <= 0) {
                Toast.makeText(this, "Please enter a valid time (greater than 0).", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simulate successful payment
            val amountPaid = calculatePayment(timeInMinutes.toInt())
            onPaymentSuccess(amountPaid, timeInMinutes.toInt())
        }

        // Set up Back button click listener
        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }
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

    // Calculate the payment based on the time (example: $0.025 per minute)
    private fun calculatePayment(minutes: Int): Float {
        val ratePerMinute = 0.025f // Example rate of $0.025 per minute
        return minutes * ratePerMinute
    }
}