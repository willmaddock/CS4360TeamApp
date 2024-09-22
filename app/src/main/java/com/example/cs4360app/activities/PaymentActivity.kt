package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R

class PaymentActivity : AppCompatActivity() {

    private lateinit var guestEmailInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val payAsGuestButton = findViewById<Button>(R.id.pay_as_guest_button)
        val loginButton = findViewById<Button>(R.id.login_button)
        guestEmailInput = findViewById(R.id.guest_email_input)

        payAsGuestButton.setOnClickListener {
            proceedWithGuestCheckout()
        }

        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun proceedWithGuestCheckout() {
        val guestEmail = guestEmailInput.text.toString().trim()

        // Check if the email is valid
        if (isValidEmail(guestEmail)) {
            val paymentDetails = collectPaymentDetails()
            processPayment(guestEmail, paymentDetails)
        } else {
            showError("Please enter a valid email.")
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun processPayment(email: String, paymentDetails: PaymentDetails) {
        // Implement payment processing logic here
        Toast.makeText(this, "Payment processed for $email", Toast.LENGTH_SHORT).show()
    }

    private fun collectPaymentDetails(): PaymentDetails {
        // Implement logic to collect payment details
        return PaymentDetails(amount = 0.0, currency = "USD") // Example values
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

data class PaymentDetails(
    val amount: Double,  // Amount for the payment
    val currency: String // Currency type (e.g., USD, EUR)
)