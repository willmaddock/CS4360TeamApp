package com.example.cs4360app.managers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.view.View
import com.example.cs4360app.activities.*
import com.example.cs4360app.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainMenuManager(
    private val context: Context,
    private val binding: ActivityMainBinding
) {
    private var countDownTimer: CountDownTimer? = null // Declare a variable to hold the timer

    fun initializeMenu() {
        setupClickListeners()
        checkAndShowActiveTimer() // Check if timer is active and update UI accordingly
    }

    private fun setupClickListeners() {
        // Submit Review
        binding.btnSubmitReview.setOnClickListener {
            context.startActivity(Intent(context, SubmitReviewActivity::class.java))
        }

        // Survey
        binding.buttonSurvey.setOnClickListener {
            context.startActivity(Intent(context, SurveyActivity::class.java))
        }

        // Open Map
        binding.mapButton.setOnClickListener {
            context.startActivity(Intent(context, MapsActivity::class.java))
        }

        // Petition
        binding.buttonPetition.setOnClickListener {
            context.startActivity(Intent(context, PetitionActivity::class.java))
        }

        // Notifications
        binding.notificationButton.setOnClickListener {
            context.startActivity(Intent(context, NotificationsActivity::class.java))
        }

        // Chat
        binding.chatButton.setOnClickListener {
            context.startActivity(Intent(context, ChatActivity::class.java))
        }

        // Parking Budget Simulator (Payment Button)
        binding.paymentButton.setOnClickListener {
            context.startActivity(Intent(context, ParkingBudgetSimulatorActivity::class.java))
        }

        // Timer Button
        binding.timerButton.setOnClickListener {
            context.startActivity(Intent(context, TimerActivity::class.java))
        }
    }

    @SuppressLint("DefaultLocale")
    fun checkAndShowActiveTimer() {
        val sharedPreferences = context.getSharedPreferences("payment_prefs", Context.MODE_PRIVATE)
        val endTime = sharedPreferences.getLong("end_time", 0)

        // Check if the timer is active
        if (endTime > System.currentTimeMillis()) {
            showActiveTimer(endTime)
            // No need to hide any buttons; all buttons remain visible
        } else {
            binding.timerButton.visibility = View.GONE // Hide timer button if not active
            countDownTimer?.cancel() // Cancel any active timer
        }
    }

    private fun showActiveTimer(endTime: Long) {
        val remainingTime = endTime - System.currentTimeMillis()
        binding.timerButton.visibility = View.VISIBLE
        startTimer(remainingTime)
    }

    @SuppressLint("SetTextI18n")
    private fun startTimer(timeInMillis: Long) {
        countDownTimer?.cancel() // Cancel any existing timer before starting a new one
        binding.timerButton.setBackgroundColor(Color.GREEN) // Set initial color to green
        binding.timerButton.text = "Time Remaining: ${formatTime(timeInMillis)}"

        countDownTimer = object : CountDownTimer(timeInMillis, 1000) {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                binding.timerButton.text = "Time Remaining: ${formatTime(millisUntilFinished)}"

                // Change button color based on remaining time
                when {
                    millisUntilFinished < (timeInMillis * 0.60) && millisUntilFinished >= (timeInMillis * 0.15) -> {
                        // 60% done
                        binding.timerButton.setBackgroundColor(Color.YELLOW)
                    }
                    millisUntilFinished < (timeInMillis * 0.15) -> {
                        // 15 minutes left
                        binding.timerButton.setBackgroundColor(Color.RED)
                    }
                    else -> {
                        binding.timerButton.setBackgroundColor(Color.GREEN)
                    }
                }
            }

            override fun onFinish() {
                binding.timerButton.visibility = View.GONE // Hide the timer button once finished
                countDownTimer = null // Clear the timer instance
            }
        }.start()
    }

    @SuppressLint("DefaultLocale")
    private fun formatTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}