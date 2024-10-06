package com.example.cs4360app.activities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs4360app.databinding.ActivityMainBinding
import com.example.cs4360app.managers.MainMenuManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mainMenuManager: MainMenuManager

    companion object {
        private var instance: MainActivity? = null

        fun getInstance(): MainActivity? {
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        instance = this // Set instance when the activity is created
        auth = FirebaseAuth.getInstance()

        // Initialize MainMenuManager
        mainMenuManager = MainMenuManager(this, binding, auth)
        mainMenuManager.initializeMenu()

        // Setup recycler view for reviews
        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(this)
        fetchReviews()

        // Check if timer is active and show it
        if (isTimerActive()) {
            showActiveTimer()
        } else {
            binding.timerButton.visibility = View.GONE // Hide timer button if not active
        }
    }

    private fun fetchReviews() {
        // Your fetch reviews code...
    }

    private fun isTimerActive(): Boolean {
        val sharedPreferences = getSharedPreferences("payment_prefs", Context.MODE_PRIVATE)
        val endTime = sharedPreferences.getLong("end_time", 0)
        return endTime > System.currentTimeMillis()
    }

    private fun showActiveTimer() {
        val sharedPreferences = getSharedPreferences("payment_prefs", Context.MODE_PRIVATE)
        val endTime = sharedPreferences.getLong("end_time", 0)
        val remainingTime = endTime - System.currentTimeMillis()

        if (remainingTime > 0) {
            binding.timerButton.visibility = View.VISIBLE
            startTimer(remainingTime)
        } else {
            binding.timerButton.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startTimer(timeInMillis: Long) {
        binding.timerButton.setBackgroundColor(Color.GREEN) // Set initial color to green
        binding.timerButton.text = "Time Remaining: ${formatTime(timeInMillis)}"

        object : CountDownTimer(timeInMillis, 1000) {
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

    override fun onDestroy() {
        super.onDestroy()
        instance = null // Clear instance when activity is destroyed
    }
}