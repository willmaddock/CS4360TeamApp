package com.example.cs4360app

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs4360app.activities.*
import com.example.cs4360app.adapters.ReviewAdapter
import com.example.cs4360app.databinding.ActivityMainBinding
import com.example.cs4360app.models.Review
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val MAX_COST = 10.0 // Example value
        private const val TAG = "MainActivity"
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

        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(this)
        fetchReviews()
        setupClickListeners()
        updateUI()
    }

    private fun setupClickListeners() {
        binding.btnSubmitReview.setOnClickListener {
            Log.d(TAG, "Submit Review Button Clicked")
            startActivity(Intent(this, SubmitReviewActivity::class.java))
        }

        binding.buttonSurvey.setOnClickListener {
            Log.d(TAG, "Survey Button Clicked")
            startActivity(Intent(this, SurveyActivity::class.java))
        }

        binding.mapButton.setOnClickListener {
            Log.d(TAG, "Map Button Clicked")
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("maxCost", MAX_COST)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            Log.d(TAG, "Login Button Clicked")
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.logoutButton.setOnClickListener {
            Log.d(TAG, "Logout Button Clicked")
            logoutUser()
        }

        binding.buttonPetition.setOnClickListener {
            Log.d(TAG, "Submit Petition Button Clicked")
            startActivity(Intent(this, PetitionActivity::class.java))
        }

        binding.paymentButton.setOnClickListener {
            Log.d(TAG, "Payment Button Clicked")
            startActivity(Intent(this, SelectParkingLot::class.java))
        }

        // New Notification Button Click Listener
        binding.notificationButton.setOnClickListener {
            Log.d(TAG, "Notification Button Clicked")
            // Add your code to handle notifications, such as opening a notifications activity or displaying a dialog
            startActivity(Intent(this, NotificationsActivity::class.java)) // Assume you have a NotificationsActivity
        }
    }

    private fun fetchReviews() {
        db.collection("reviews").get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.d(TAG, "No reviews found")
                    binding.reviewRecyclerView.adapter = ReviewAdapter(emptyList())
                    Toast.makeText(this, "No reviews available", Toast.LENGTH_SHORT).show()
                } else {
                    val reviews = documents.toObjects(Review::class.java)
                    Log.d(TAG, "Fetched ${reviews.size} reviews")
                    binding.reviewRecyclerView.adapter = ReviewAdapter(reviews)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching reviews", exception)
                Toast.makeText(this, "Error fetching reviews. Please try again.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUI() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.loginButton.visibility = View.GONE // Hide login button
            binding.logoutButton.visibility = View.VISIBLE // Show logout button
            binding.buttonPetition.visibility = View.VISIBLE // Show submit petition button
        } else {
            binding.loginButton.visibility = View.VISIBLE // Show login button
            binding.logoutButton.visibility = View.GONE // Hide logout button
            binding.buttonPetition.visibility = View.GONE // Hide submit petition button
        }
    }

    private fun logoutUser() {
        auth.signOut()
        updateUI()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null // Clear the instance when the activity is destroyed
    }

    // Method to show a notification dialog (if needed)
    fun showNotificationDialog(title: String, message: String) {
        runOnUiThread {
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }
}