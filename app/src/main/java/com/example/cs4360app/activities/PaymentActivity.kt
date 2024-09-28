package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R
import com.google.firebase.auth.FirebaseAuth

class PaymentActivity : AppCompatActivity() {

    private lateinit var guestEmailInput: EditText
    private lateinit var payAsGuestButton: Button
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Find views by ID
        guestEmailInput = findViewById(R.id.guest_email_input)
        payAsGuestButton = findViewById(R.id.pay_as_guest_button)
        loginButton = findViewById(R.id.login_button)

        // Check if the user is logged in
        if (auth.currentUser != null) {
            // User is logged in, proceed to select parking lot
            proceedToSelectParkingLot()
        } else {
            // User is not logged in, allow guest payment
            setupGuestPayment()
        }

        // Login button action
        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun setupGuestPayment() {
        // Show guest email input and handle guest checkout
        guestEmailInput.visibility = EditText.VISIBLE
        payAsGuestButton.visibility = Button.VISIBLE
        payAsGuestButton.setOnClickListener {
            proceedWithGuestCheckout()
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

    private fun proceedToSelectParkingLot() {
        // Launch the SelectParkingLot activity when logged in
        startActivity(Intent(this, SelectParkingLot::class.java))
        finish() // Optionally, finish the PaymentActivity if you don't want it in the back stack
    }

    // Helper function to validate email format
    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Function to process payment
    private fun processPayment(email: String, paymentDetails: PaymentDetails) {
        // Implement payment processing logic here
        Toast.makeText(this, "Payment processed for $email", Toast.LENGTH_SHORT).show()
    }

    // Function to collect payment details
    private fun collectPaymentDetails(): PaymentDetails {
        // Placeholder for payment details logic
        return PaymentDetails(amount = 0.0, currency = "USD") // Example values
    }

    // Function to show error messages
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

data class PaymentDetails(
    val amount: Double,  // Amount for the payment
    val currency: String // Currency type (e.g., USD, EUR)
)