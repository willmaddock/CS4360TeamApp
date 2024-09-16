package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R
import com.example.cs4360app.models.MSUDCampusLocation
import com.example.cs4360app.models.ParkingLot
import com.example.cs4360app.models.Review
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.example.cs4360app.MainActivity

class SubmitReviewActivity : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var commentEditText: EditText
    private lateinit var submitReviewButton: Button
    private lateinit var averageRatingTextView: TextView
    private lateinit var reviewCountTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var parkingLotSpinner: Spinner
    private lateinit var categorySpinner: Spinner
    private lateinit var parkingLotDetailsTextView: TextView
    private lateinit var uploadImageButton: Button
    private val db = FirebaseFirestore.getInstance()
    private lateinit var parkingLots: List<ParkingLot>
    private val IMAGE_REQUEST_CODE = 1001
    private var selectedImageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_review)

        // Initialize UI elements
        ratingBar = findViewById(R.id.ratingBar)
        commentEditText = findViewById(R.id.commentEditText)
        submitReviewButton = findViewById(R.id.submitReviewButton)
        averageRatingTextView = findViewById(R.id.averageRatingTextView)
        reviewCountTextView = findViewById(R.id.reviewCountTextView)
        progressBar = findViewById(R.id.progressBar)
        parkingLotSpinner = findViewById(R.id.parkingLotSpinner)
        categorySpinner = findViewById(R.id.categorySpinner)
        parkingLotDetailsTextView = findViewById(R.id.parkingLotDetailsTextView)
        uploadImageButton = findViewById(R.id.uploadImageButton)

        // Example data for parking lots
        parkingLots = listOf(
            ParkingLot("1", "Jordan Parking Garage", 10.0, 4.5f, MSUDCampusLocation.JORDAN_PARKING_GARAGE, true),
            ParkingLot("2", "Tivoli Parking Garage", 15.0, 4.0f, MSUDCampusLocation.TIVOLI_PARKING_LOT, false),
            ParkingLot("3", "Auraria West", 8.0, 3.8f, MSUDCampusLocation.AURARIA_WEST, true),
            ParkingLot("4", "Auraria East", 12.0, 4.2f, MSUDCampusLocation.AURARIA_EAST, true),
            ParkingLot("5", "Ninth and Walnut", 5.0, 4.6f, MSUDCampusLocation.NINTH_AND_WALNUT, false)
        )

        // Setup parking lot spinner
        val parkingLotAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, parkingLots.map { it.name })
        parkingLotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        parkingLotSpinner.adapter = parkingLotAdapter

        // Setup category spinner
        val categories = arrayOf("Ease of Finding Spot", "Security", "Cleanliness")
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        // Handle image upload
        uploadImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        }

        // Handle review submission
        submitReviewButton.setOnClickListener {
            submitReview()
        }

        // Fetch and display existing reviews
        fetchReviews()
    }

    private fun submitReview() {
        val rating = ratingBar.rating
        val comment = commentEditText.text.toString()
        val parkingLot = parkingLots[parkingLotSpinner.selectedItemPosition]
        val category = categorySpinner.selectedItem.toString()

        if (rating > 0 && comment.isNotBlank()) {
            val review = Review(
                userId = "user123",
                parkingLotId = parkingLot.id,
                rating = rating,
                comment = comment,
                timestamp = System.currentTimeMillis()
            )
            // Upload review to Firestore
            db.collection("reviews").add(review)
                .addOnSuccessListener {
                    Toast.makeText(this, "Review submitted successfully", Toast.LENGTH_SHORT).show()
                    // Navigate back to the main screen
                    navigateToMainScreen()
                }
                .addOnFailureListener { e ->
                    Log.e("SubmitReviewActivity", "Error submitting review", e)
                    Toast.makeText(this, "Error submitting review", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please provide a rating and comment", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchReviews() {
        db.collection("reviews").get()
            .addOnSuccessListener { result ->
                var totalRating = 0f
                var reviewCount = 0
                result.forEach { document ->
                    val review = document.toObject(Review::class.java)
                    totalRating += review.rating
                    reviewCount++
                }
                val averageRating = if (reviewCount > 0) totalRating / reviewCount else 0f
                averageRatingTextView.text = "Average Rating: ${String.format("%.1f", averageRating)}"
                reviewCountTextView.text = "Total Reviews: $reviewCount"
            }
            .addOnFailureListener { e ->
                Log.e("SubmitReviewActivity", "Error fetching reviews", e)
            }
    }

    private fun navigateToMainScreen() {
        // Create an intent to start the MainActivity
        val intent = Intent(this, MainActivity::class.java)

        // Optional: Add flags to clear the activity stack
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

        // Start the MainActivity
        startActivity(intent)

        // Finish the current activity to remove it from the stack
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data.toString()
            Toast.makeText(this, "Image selected: $selectedImageUri", Toast.LENGTH_SHORT).show()
            // Handle image upload to Firestore if needed
        }
    }
}