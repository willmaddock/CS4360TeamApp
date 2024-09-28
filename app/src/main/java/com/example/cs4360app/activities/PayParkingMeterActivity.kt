package com.example.cs4360app.activities

import PaymentDialog
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R

class PayParkingMeterActivity : AppCompatActivity() {
    private lateinit var licensePlateInput: EditText
    private lateinit var timeInput: EditText // EditText for time input
    private lateinit var payButton: Button
    private lateinit var timerTextView: TextView
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_parking_meter)

        // Initialize views
        licensePlateInput = findViewById(R.id.license_plate_input)
        timeInput = findViewById(R.id.time_input) // Assuming you add this EditText in XML
        payButton = findViewById(R.id.pay_button)
        timerTextView = findViewById(R.id.timer_text_view)

        // Check for active timer
        checkActiveTimer()

        payButton.setOnClickListener {
            val timeInputText = timeInput.text.toString().trim()
            if (timeInputText.isNotEmpty()) {
                val timeInMinutes = timeInputText.toIntOrNull()
                if (timeInMinutes != null && timeInMinutes in 15..150) { // Validate input
                    if (canPayForParking()) {
                        processPayment(timeInMinutes)
                    } else {
                        Toast.makeText(this, "You have an active timer. Please wait until it expires.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Enter a valid time between 15 and 150 minutes", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a time", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun processPayment(timeInMinutes: Int) {
        // Check if payment info exists
        val savedCardNumber = getPaymentInfo()

        if (savedCardNumber != null) {
            // If payment information is available, proceed with payment
            Toast.makeText(this, "Payment of $$timeInMinutes for parking meter collected using saved card: $savedCardNumber", Toast.LENGTH_SHORT).show()
            startTimer(timeInMinutes)
        } else {
            // Prompt for payment information
            showPaymentDialog(timeInMinutes)
        }
    }

    private fun showPaymentDialog(timeInMinutes: Int) {
        val dialog = PaymentDialog(this) { cardNumber ->
            savePaymentInfo(cardNumber) // Save card number
            Toast.makeText(this, "Payment of $$timeInMinutes for parking meter collected using card: $cardNumber", Toast.LENGTH_SHORT).show()
            startTimer(timeInMinutes)
        }
        dialog.show()
    }

    private fun savePaymentInfo(cardNumber: String) {
        val sharedPreferences = getSharedPreferences("payment_info", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("card_number", cardNumber)
            apply()
        }
    }

    private fun getPaymentInfo(): String? {
        val sharedPreferences = getSharedPreferences("payment_info", MODE_PRIVATE)
        return sharedPreferences.getString("card_number", null)
    }

    private fun startTimer(timeInMinutes: Int) {
        val totalTimeInMillis = timeInMinutes * 60 * 1000L // Convert minutes to milliseconds
        val timerEndTime = System.currentTimeMillis() + totalTimeInMillis // Save end time
        saveTimerEndTime(timerEndTime)

        timer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / (1000 * 60)) % 60
                timerTextView.text = String.format("Timer: %02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timerTextView.text = "Timer: 00:00"
                resetTimer()
                Toast.makeText(this@PayParkingMeterActivity, "Parking meter expired!", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun saveTimerEndTime(endTime: Long) {
        val sharedPreferences = getSharedPreferences("timer_info", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putLong("timer_end_time", endTime)
            apply()
        }
    }

    private fun checkActiveTimer() {
        val sharedPreferences = getSharedPreferences("timer_info", MODE_PRIVATE)
        val timerEndTime = sharedPreferences.getLong("timer_end_time", 0)

        if (timerEndTime > System.currentTimeMillis()) {
            // Timer is active
            val remainingTime = timerEndTime - System.currentTimeMillis()
            startCountDownTimer(remainingTime)
        }
    }

    private fun startCountDownTimer(remainingTimeInMillis: Long) {
        timer = object : CountDownTimer(remainingTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / (1000 * 60)) % 60
                timerTextView.text = String.format("Timer: %02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timerTextView.text = "Timer: 00:00"
                resetTimer()
            }
        }.start()
    }

    private fun resetTimer() {
        val sharedPreferences = getSharedPreferences("timer_info", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("timer_end_time")
            apply()
        }
    }

    private fun canPayForParking(): Boolean {
        val sharedPreferences = getSharedPreferences("timer_info", MODE_PRIVATE)
        val timerEndTime = sharedPreferences.getLong("timer_end_time", 0)

        return timerEndTime <= System.currentTimeMillis() // Timer must be expired to pay
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel() // Cancel timer if the activity is destroyed
    }
}