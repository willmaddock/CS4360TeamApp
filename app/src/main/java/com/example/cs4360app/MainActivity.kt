package com.example.cs4360app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs4360app.activities.*
import com.example.cs4360app.adapters.ReviewAdapter
import com.example.cs4360app.databinding.ActivityMainBinding
import com.example.cs4360app.models.Review
import com.example.cs4360app.models.Petition
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Set up the RecyclerView for displaying reviews
        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch reviews from Firestore and display them in the RecyclerView
        fetchReviews()

        // Set up button click listeners
        setupClickListeners()

        // Update UI based on the current authentication state
        updateUI()
    }

    private fun setupClickListeners() {
        // Handle the "Submit Review" button click
        binding.btnSubmitReview.setOnClickListener {
            Log.d(TAG, "Submit Review Button Clicked")
            startActivity(Intent(this, SubmitReviewActivity::class.java))
        }

        // Handle the "Survey" button click
        binding.buttonSurvey.setOnClickListener {
            Log.d(TAG, "Survey Button Clicked")
            startActivity(Intent(this, SurveyActivity::class.java))
        }

        // Handle the "Map" button click
        binding.mapButton.setOnClickListener {
            Log.d(TAG, "Map Button Clicked")
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("maxCost", MAX_COST) // Example: Pass a maximum cost parameter
            startActivity(intent)
        }

        // Handle the "Login" button click
        binding.loginButton.setOnClickListener {
            Log.d(TAG, "Login Button Clicked")
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Handle the "Logout" button click
        binding.logoutButton.setOnClickListener {
            Log.d(TAG, "Logout Button Clicked")
            logoutUser()
        }

        // Handle the "Submit Petition" button click
        binding.buttonPetition.setOnClickListener {
            Log.d(TAG, "Submit Petition Button Clicked")
            startActivity(Intent(this, PetitionActivity::class.java))
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
            binding.loginButton.visibility = View.GONE
            binding.logoutButton.visibility = View.VISIBLE
        } else {
            binding.loginButton.visibility = View.VISIBLE
            binding.logoutButton.visibility = View.GONE
        }
    }

    private fun logoutUser() {
        auth.signOut()
        updateUI()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
    }
}