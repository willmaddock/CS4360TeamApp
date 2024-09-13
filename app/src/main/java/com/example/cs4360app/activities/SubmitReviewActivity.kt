package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.models.Review
import com.google.firebase.firestore.FirebaseFirestore
import android.view.View
import android.util.Log
import android.widget.ProgressBar
import com.example.cs4360app.MainActivity
import com.example.cs4360app.R

class SubmitReviewActivity : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var commentEditText: EditText
    private lateinit var submitReviewButton: Button
    private lateinit var averageRatingTextView: TextView
    private lateinit var reviewCountTextView: TextView
    private lateinit var progressBar: ProgressBar
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_review) // Ensure this matches your XML layout name

        // Initialize UI elements
        ratingBar = findViewById(R.id.ratingBar)
        commentEditText = findViewById(R.id.commentEditText)
        submitReviewButton = findViewById(R.id.submitReviewButton)
        averageRatingTextView = findViewById(R.id.averageRatingTextView)
        reviewCountTextView = findViewById(R.id.reviewCountTextView)
        progressBar = findViewById(R.id.progressBar) // Ensure you have a ProgressBar in your XML layout

        // Set click listener for the submit button
        submitReviewButton.setOnClickListener {
            submitReview()
        }

        // Fetch and display review statistics
        fetchReviewStats()
    }

    private fun getUserId(): String {
        // Replace this with actual logic to retrieve user ID
        return "currentUserId"
    }

    private fun getParkingLotId(): String {
        // Replace this with actual logic to retrieve parking lot ID
        return "currentParkingLotId"
    }

    private fun submitReview() {
        val rating = ratingBar.rating
        val comment = commentEditText.text.toString().trim()

        // Validate input
        if (rating <= 0 || comment.isEmpty()) {
            Toast.makeText(this, "Please provide both rating and comment", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a review object
        val userId = getUserId()
        val parkingLotId = getParkingLotId()
        val review = Review(userId, parkingLotId, rating, comment)

        showLoading(true)
        saveReviewToFirestore(review)
    }

    private fun saveReviewToFirestore(review: Review) {
        db.collection("reviews").add(review)
            .addOnSuccessListener {
                Toast.makeText(this, "Review submitted successfully.", Toast.LENGTH_SHORT).show()
                commentEditText.text.clear()
                ratingBar.rating = 0f
                fetchReviewStats() // Update the stats after submission
                showLoading(false)

                // Navigate back to the home screen
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish() // This will close the SubmitReviewActivity
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to submit review: ${e.message}", Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
    }

    private fun fetchReviewStats() {
        showLoading(true)
        db.collection("reviews").get()
            .addOnSuccessListener { documents ->
                val reviews = documents.toObjects(Review::class.java)

                // Log the number of reviews fetched
                Log.d("SubmitReviewActivity", "Reviews fetched: ${reviews.size}")

                // Calculate average rating
                val totalStars = reviews.sumOf { it.rating.toDouble() }
                val averageRating = if (reviews.isNotEmpty()) totalStars / reviews.size else 0.0

                // Update the average rating and review count TextViews
                val reviewCount = reviews.size
                averageRatingTextView.text = "Average Rating: %.1f".format(averageRating)
                reviewCountTextView.text = "Total Reviews: $reviewCount"
                showLoading(false)
            }
            .addOnFailureListener { exception ->
                Log.e("SubmitReviewActivity", "Error fetching review stats", exception)
                Toast.makeText(this, "Error fetching review stats: ${exception.message}", Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}