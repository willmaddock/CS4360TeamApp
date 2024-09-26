package com.example.cs4360app.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cs4360app.MainActivity
import com.example.cs4360app.R

class ParkingTimer : AppCompatActivity() {
    private lateinit var timerTextView: TextView
    private lateinit var end_timerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_timer)

        timerTextView = findViewById(R.id.timerTextView)
        end_timerTextView = findViewById(R.id.end_timerTextView)

        val menuButton: Button = findViewById(R.id.menuButton)
        menuButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // Define the BroadcastReceiver
    // Define the BroadcastReceiver
    private val countdownReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val remainingTime = intent.getLongExtra(TimerService.COUNTDOWN_TIME, 0)
            val timerFinished = intent.getBooleanExtra("timer_finished", false)

            if (timerFinished) {
                end_timerTextView.text = "Your time has expired for this parking lot. If you want to get more time, you can pay another ticket"
            } else {
                // Update the UI with the remaining time
                val seconds = (remainingTime / 1000) % 60
                val minutes = (remainingTime / (1000 * 60)) % 60
                val hours = (remainingTime / (1000 * 60 * 60)) % 24
                timerTextView.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }
            Log.d("ParkingTimer", "Received broadcast with remaining time: $remainingTime")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        // Register the receiver
        registerReceiver(countdownReceiver, IntentFilter(TimerService.COUNTDOWN_BR),
            RECEIVER_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        // Unregister the receiver
        unregisterReceiver(countdownReceiver)
    }
}