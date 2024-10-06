package com.example.cs4360app.managers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.example.cs4360app.activities.*
import com.example.cs4360app.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class MainMenuManager(
    private val context: Context,
    private val binding: ActivityMainBinding,
    private val auth: FirebaseAuth
) {

    fun initializeMenu() {
        setupClickListeners()
        updateUI()
        checkAndShowActiveTimer() // Check if timer is active and update UI accordingly
    }

    private fun setupClickListeners() {
        binding.btnSubmitReview.setOnClickListener {
            context.startActivity(Intent(context, SubmitReviewActivity::class.java))
        }

        binding.buttonSurvey.setOnClickListener {
            context.startActivity(Intent(context, SurveyActivity::class.java))
        }

        binding.mapButton.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            intent.putExtra("maxCost", 10.0) // Example max cost
            context.startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }

        binding.logoutButton.setOnClickListener {
            logoutUser()
        }

        binding.buttonPetition.setOnClickListener {
            context.startActivity(Intent(context, PetitionActivity::class.java))
        }

        binding.paymentButton.setOnClickListener {
            context.startActivity(Intent(context, SelectParkingLotActivity::class.java))
        }

        binding.notificationButton.setOnClickListener {
            context.startActivity(Intent(context, NotificationsActivity::class.java))
        }

        binding.chatButton.setOnClickListener {
            context.startActivity(Intent(context, ChatActivity::class.java))
        }
    }

    // Method to update UI based on login status
    private fun updateUI() {
        val currentUser = auth.currentUser
        binding.loginButton.visibility = if (currentUser != null) View.GONE else View.VISIBLE
        binding.logoutButton.visibility = if (currentUser != null) View.VISIBLE else View.GONE
        binding.buttonPetition.visibility = if (currentUser != null) View.VISIBLE else View.GONE
    }

    // Method to check if a timer is active and update UI accordingly
    private fun checkAndShowActiveTimer() {
        val sharedPreferences = context.getSharedPreferences("payment_prefs", Context.MODE_PRIVATE)
        val endTime = sharedPreferences.getLong("end_time", 0)
        val currentTime = System.currentTimeMillis()
        val remainingTime = endTime - currentTime

        if (remainingTime > 0) {
            binding.timerButton.visibility = View.VISIBLE
            binding.paymentButton.visibility = View.GONE // Hide payment button
            startTimer(remainingTime)
        } else {
            binding.timerButton.visibility = View.GONE
            binding.paymentButton.visibility = View.VISIBLE // Show payment button
        }
    }

    private fun startTimer(duration: Long) {
        object : CountDownTimer(duration, 1000) {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(hours)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(minutes)

                binding.timerButton.text = String.format("Time Remaining: %02d:%02d:%02d", hours, minutes, seconds)
            }

            override fun onFinish() {
                binding.timerButton.visibility = View.GONE
                binding.paymentButton.visibility = View.VISIBLE
                Toast.makeText(context, "Time expired!", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun logoutUser() {
        auth.signOut()
        Toast.makeText(context, "Successfully logged out", Toast.LENGTH_SHORT).show()
        updateUI() // Update UI after logout

        // Redirect to MapsActivity after logout
        context.startActivity(Intent(context, MapsActivity::class.java))
    }
}