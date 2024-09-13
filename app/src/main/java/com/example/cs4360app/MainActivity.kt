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
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(this)
        fetchReviews()

        // Open submit review screen
        binding.btnSubmitReview.setOnClickListener {
            Log.d("MainActivity", "Submit Review Button Clicked")
            startActivity(Intent(this, SubmitReviewActivity::class.java))
        }

        // Open survey screen
        binding.buttonSurvey.setOnClickListener {
            Log.d("MainActivity", "Survey Button Clicked")
            startActivity(Intent(this, SurveyActivity::class.java))
        }

        // Open map screen
        binding.mapButton.setOnClickListener {
            Log.d("MainActivity", "Map Button Clicked")
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    private fun fetchReviews() {
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