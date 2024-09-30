package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.MainActivity
import com.example.cs4360app.R
import com.google.firebase.auth.FirebaseAuth

class PaymentActivity : AppCompatActivity() {

    private lateinit var guestEmailInput: EditText
    private lateinit var payAsGuestButton: Button
    private lateinit var loginButton: Button
    private lateinit var backButton: Button
    private lateinit var auth: FirebaseAuth

    private var fromMainMenu: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Get intent data
        fromMainMenu = intent.getBooleanExtra("fromMainMenu", false)

        // Find views by ID
        guestEmailInput = findViewById(R.id.guest_email_input)
        payAsGuestButton = findViewById(R.id.pay_as_guest_button)
        loginButton = findViewById(R.id.login_button)
        backButton = findViewById(R.id.back_button)

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

        // Back button action
        backButton.setOnClickListener {
            if (auth.currentUser != null && fromMainMenu) {
                navigateToMainMenu()
            } else {
                navigateToMaps()
            }
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
            // Navigate to SelectParkingLotActivity after guest email entry
            val intent = Intent(this, SelectParkingLotActivity::class.java)
            intent.putExtra("guestEmail", guestEmail) // Pass the guest email
            startActivity(intent)
            finish() // Close the PaymentActivity
        } else {
            showError("Please enter a valid email.")
        }
    }

    private fun proceedToSelectParkingLot() {
        // Launch the SelectParkingLot activity when logged in
        startActivity(Intent(this, SelectParkingLotActivity::class.java))
        finish() // Close PaymentActivity to prevent going back
    }

    // Helper function to validate email format
    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Function to show error messages
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Navigate back to Maps activity
    private fun navigateToMaps() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Navigate back to Main Menu
    private fun navigateToMainMenu() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        // Navigate back to MapsActivity when back is pressed
        navigateToMaps()
    }
}