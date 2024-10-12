package com.example.cs4360app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R

class TimerActivity : AppCompatActivity() {

    private lateinit var amountPaidTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var backToMapsButton: Button
    private var timer: CountDownTimer? = null
    private var remainingTime: Long = 0 // Time in milliseconds

    private val maxCost: Float = 5.35f // Maximum cost for 2 hours and 30 minutes
    private val costPerMinute: Float = maxCost / 150 // Cost per minute

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        // Bind UI elements
        amountPaidTextView = findViewById(R.id.amount_paid_text_view)
        timerTextView = findViewById(R.id.timer_text_view)
        backToMapsButton = findViewById(R.id.back_to_maps_button)

        // Retrieve the end time from SharedPreferences
        val sharedPreferences = getSharedPreferences("payment_prefs", MODE_PRIVATE)
        val endTime = sharedPreferences.getLong("end_time", 0)

        // Calculate remaining time
        remainingTime = endTime - System.currentTimeMillis()
        if (remainingTime < 0) {
            remainingTime = 0 // If time is already expired
        }

        // Calculate the amount paid
        val amountPaid = calculateAmount(remainingTime / 1000)

        // Display the amount paid
        amountPaidTextView.text = String.format("Amount Paid: $%.2f", amountPaid)

        // Start the timer if there's time remaining
        if (remainingTime > 0) {
            startTimer(remainingTime)
        } else {
            timerTextView.text = "Time's up!"
            Toast.makeText(this, "Parking time expired!", Toast.LENGTH_SHORT).show()
            navigateToMaps()
        }

        // Set up button listeners
        backToMapsButton.setOnClickListener {
            navigateToMaps()
        }
    }

    private fun calculateAmount(durationInSeconds: Long): Float {
        val durationInMinutes = durationInSeconds / 60
        return durationInMinutes * costPerMinute
    }

    private fun startTimer(milliseconds: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(milliseconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimerDisplay(millisUntilFinished)
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                timerTextView.text = "Time's up!"
                Toast.makeText(this@TimerActivity, "Parking time expired!", Toast.LENGTH_SHORT).show()

                // Update SharedPreferences when the timer expires
                val sharedPreferences = getSharedPreferences("payment_prefs", MODE_PRIVATE)
                sharedPreferences.edit().putBoolean("payment_active", false).apply()

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
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}