package com.example.cs4360app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.MainActivity
import com.example.cs4360app.R

@Suppress("SameParameterValue")
class TimerActivity : AppCompatActivity() {

    private lateinit var amountPaidTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var backToMainMenuButton: Button
    private lateinit var backToMapsButton: Button
    private var timer: CountDownTimer? = null
    private var remainingTime: Long = 0 // Time in seconds

    private val maxCost: Float = 5.35f // Maximum cost for 2 hours and 30 minutes
    private val costPerMinute: Float = maxCost / 150 // Cost per minute

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        // Bind UI elements
        amountPaidTextView = findViewById(R.id.amount_paid_text_view)
        timerTextView = findViewById(R.id.timer_text_view)
        backToMainMenuButton = findViewById(R.id.back_to_main_menu_button)
        backToMapsButton = findViewById(R.id.back_to_maps_button)

        // Check if payment is active and the remaining time is set
        val sharedPreferences = getSharedPreferences("payment_prefs", MODE_PRIVATE)
        remainingTime = sharedPreferences.getLong("remaining_time", 0)

        // Check if user is logged in
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false) // Adjust according to your login logic

        if (!isPaymentActive() || remainingTime == 0L) {
            // Redirect to Maps if no payment is active
            showToastAndRedirect("Payment not completed. Redirecting to Maps.", MapsActivity::class.java)
            return
        }

        // Set button visibility based on login status
        if (isLoggedIn) {
            backToMainMenuButton.visibility = Button.VISIBLE // Show "Back to Main Menu" button for logged-in users
        } else {
            backToMainMenuButton.visibility = Button.GONE // Hide it for guests
        }

        // Calculate the amount paid
        val amountPaid = calculateAmount(remainingTime)

        // Display the amount paid
        amountPaidTextView.text = String.format("Amount Paid: $%.2f", amountPaid)

        // Start the timer
        startTimer(remainingTime)

        // Set up button listeners
        backToMainMenuButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        backToMapsButton.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
            finish()
        }
    }

    private fun isPaymentActive(): Boolean {
        val sharedPreferences = getSharedPreferences("payment_prefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean("payment_active", false) && remainingTime > 0
    }

    private fun calculateAmount(durationInSeconds: Long): Float {
        // Convert duration from seconds to minutes
        val durationInMinutes = durationInSeconds / 60
        return durationInMinutes * costPerMinute
    }

    private fun startTimer(seconds: Long) {
        // Cancel any existing timer before starting a new one
        timer?.cancel()

        timer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimerDisplay(millisUntilFinished)
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                timerTextView.text = "Time's up!"
                Toast.makeText(this@TimerActivity, "Parking time expired!", Toast.LENGTH_SHORT).show()
                navigateToMaps()
            }
        }.start()
    }

    @SuppressLint("DefaultLocale")
    private fun updateTimerDisplay(millisUntilFinished: Long) {
        val secondsLeft = (millisUntilFinished / 1000).toInt()
        val minutesLeft = secondsLeft / 60
        val remainingSeconds = secondsLeft % 60
        timerTextView.text = String.format("Time Remaining: %02d:%02d", minutesLeft, remainingSeconds)
    }

    private fun navigateToMaps() {
        startActivity(Intent(this, MapsActivity::class.java))
        finish()
    }

    private fun showToastAndRedirect(message: String, destinationActivity: Class<*>) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, destinationActivity))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel() // Cancel the timer when the activity is destroyed
    }
}