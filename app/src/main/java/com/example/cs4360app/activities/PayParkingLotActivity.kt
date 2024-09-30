package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R

class PayParkingLotActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_parking_lot)

        // Pay Button click listener
        val payButton: Button = findViewById(R.id.payButton)
        payButton.setOnClickListener { validateAndProceed() }

        // Back Button click listener (optional)
        val backButton: Button = findViewById(R.id.back_button)
        backButton.setOnClickListener { finish() } // Finish activity and return to the previous one
    }

    private fun validateAndProceed() {
        // Get references to the input fields
        val cardName: EditText = findViewById(R.id.cardname)
        val cardNumber: EditText = findViewById(R.id.cardnumber)
        val expireDate: EditText = findViewById(R.id.expire_date)
        val cvc: EditText = findViewById(R.id.cvc)

        // Extract input values
        val cardNameStr = cardName.text.toString()
        val cardNumberStr = cardNumber.text.toString()
        val expireDateStr = expireDate.text.toString()
        val cvcStr = cvc.text.toString()

        // Validate inputs
        val validationResult = validateInputs(cardNameStr, cardNumberStr, expireDateStr, cvcStr)

        if (validationResult.first) {
            // Save payment information locally
            savePaymentInfo(cardNameStr, cardNumberStr, expireDateStr, cvcStr)

            // Display confirmation message
            Toast.makeText(this, "Payment Completed. Thank you for paying your parking ticket", Toast.LENGTH_LONG).show()

            // Set timer duration (for example, 60 minutes)
            val timerDuration: Long = 60 * 60 * 1000 // 60 minutes in milliseconds

            // Redirect to PayParkingMeterActivity
            val intent = Intent(this, PayParkingMeterActivity::class.java)
            intent.putExtra("timerDuration", timerDuration) // Pass timer duration to the next activity
            startActivity(intent)
            finish() // Finish this activity
        } else {
            // Show validation error message
            Toast.makeText(this, validationResult.second, Toast.LENGTH_SHORT).show()
        }
    }

    // Function to validate user input
    private fun validateInputs(cardName: String, cardNumber: String, expireDate: String, cvc: String): Pair<Boolean, String> {
        return when {
            cardName.isEmpty() || cardNumber.isEmpty() || expireDate.isEmpty() || cvc.isEmpty() ->
                Pair(false, "Please fill in all fields")
            cardNumber.length != 16 ->
                Pair(false, "Please enter a valid 16-digit card number")
            !expireDate.matches(Regex("\\d{2}/\\d{2}")) ->
                Pair(false, "Please enter a valid expiry date in MM/YY format")
            cvc.length != 3 ->
                Pair(false, "Please enter a valid 3-digit CVC")
            else ->
                Pair(true, "")
        }
    }

    // Save payment details in SharedPreferences
    private fun savePaymentInfo(cardName: String, cardNumber: String, expireDate: String, cvc: String) {
        val sharedPreferences = getSharedPreferences("payment_info", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("card_name", cardName)
            putString("card_number", cardNumber)
            putString("expire_date", expireDate)
            putString("cvc", cvc)
            apply()
        }
    }

    // Retrieve saved payment details (if necessary)
    private fun getPaymentInfo(): Map<String, String>? {
        val sharedPreferences = getSharedPreferences("payment_info", MODE_PRIVATE)
        return if (sharedPreferences.contains("card_number")) {
            mapOf(
                "card_name" to (sharedPreferences.getString("card_name", "") ?: ""),
                "card_number" to (sharedPreferences.getString("card_number", "") ?: ""),
                "expire_date" to (sharedPreferences.getString("expire_date", "") ?: ""),
                "cvc" to (sharedPreferences.getString("cvc", "") ?: "")
            )
        } else {
            null
        }
    }
}