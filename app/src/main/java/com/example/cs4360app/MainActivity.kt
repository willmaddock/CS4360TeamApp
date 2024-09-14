package com.example.cs4360app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs4360app.activities.MapsActivity
import com.example.cs4360app.activities.SurveyActivity
import com.example.cs4360app.activities.SubmitReviewActivity // Import the activity
import com.example.cs4360app.adapters.ReviewAdapter
import com.example.cs4360app.databinding.ActivityMainBinding
import com.example.cs4360app.models.Review
import com.google.firebase.FirebaseApp // Firebase initialization
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase to avoid potential issues
        FirebaseApp.initializeApp(this)

        // Set up ViewBinding for the layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView with a LinearLayoutManager
        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(this)
        fetchReviews()

        // Open the submit review screen
        binding.btnSubmitReview.setOnClickListener {
            Log.d("MainActivity", "Submit Review Button Clicked")
            startActivity(Intent(this, SubmitReviewActivity::class.java))
        }

        // Open the survey screen
        binding.buttonSurvey.setOnClickListener {
            Log.d("MainActivity", "Survey Button Clicked")
            startActivity(Intent(this, SurveyActivity::class.java))
        }

        // Open the map screen with cost filter value
        binding.mapButton.setOnClickListener {
            Log.d("MainActivity", "Map Button Clicked")
            val intent = Intent(this, MapsActivity::class.java)
            // Pass the cost filter value (setting a default value for demonstration)
            val maxCost = 10.0
            intent.putExtra("maxCost", maxCost)
            startActivity(intent)
        }
    }

    private fun fetchReviews() {
        // Fetch reviews from Firestore and display them in the RecyclerView
        db.collection("reviews").get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.d("MainActivity", "No reviews found")
                    binding.reviewRecyclerView.adapter = ReviewAdapter(emptyList())
                    Toast.makeText(this, "No reviews available", Toast.LENGTH_SHORT).show()
                } else {
                    val reviews = documents.toObjects(Review::class.java)
                    binding.reviewRecyclerView.adapter = ReviewAdapter(reviews)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("MainActivity", "Error fetching reviews", exception)
                Toast.makeText(this, "Error fetching reviews", Toast.LENGTH_SHORT).show()
            }
    }
}