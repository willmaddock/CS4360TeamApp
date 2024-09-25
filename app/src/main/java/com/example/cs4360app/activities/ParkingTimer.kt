package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cs4360app.MainActivity
import com.example.cs4360app.R

class ParkingTimer : AppCompatActivity() {
    private lateinit var timer: CountDownTimer
    private lateinit var timerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_timer)

        timerTextView = findViewById(R.id.timerTextView) // Assuming you have a TextView with this id in your layout


        val parkingTime = intent.getIntExtra("parkingTime", 0)

        Log.d("ParkingTimer", "Received parking time: $parkingTime")

        timer = object : CountDownTimer(parkingTime.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / (1000 * 60)) % 60
                val hours = (millisUntilFinished / (1000 * 60 * 60)) % 24
                timerTextView.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }

            override fun onFinish() {
                timerTextView.text = "Parking time ended"
            }
        }.start()

        val payButton: Button = findViewById(R.id.menuButton)
        payButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel() // Prevent CountDownTimer from continuing to run when activity is destroyed
    }
}