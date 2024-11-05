package com.example.cs4360app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R
import com.example.cs4360app.models.MSUDCampusLocation
import com.example.cs4360app.models.ParkingLot
import com.example.cs4360app.models.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("DEPRECATION")
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
    private val imageRequestCode = 1001

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

        // Assign the global parkingLots list
        parkingLots = listOf(
            ParkingLot("1", "Dogwood Parking Lot", 8.00, 4.5f, MSUDCampusLocation.DOGWOOD_PARKING_LOT, true, 90, 100, "7th St & Walnut Auraria Denver, CO 80204"),
            ParkingLot("2", "Tivoli Parking Garage", 8.00, 4.0f, MSUDCampusLocation.TIVOLI_PARKING_GARAGE, true, 30, 200, "901 Walnut St Auraria Denver, CO 80204"),
            ParkingLot("3", "Cherry Parking Lot", 8.00, 3.5f, MSUDCampusLocation.CHERRY_PARKING_LOT, true, 75, 150, "605 Walnut St Auraria Denver, CO 80204"),
            ParkingLot("4", "Spruce Parking Lot", 8.00, 3.0f, MSUDCampusLocation.SPRUCE_PARKING_LOT, true, 50, 300, "800 Walnut St Auraria Denver, CO 80204"),
            ParkingLot("5", "Fir Parking Lot", 8.00, 2.5f, MSUDCampusLocation.FIR_PARKING_LOT, true, 10, 400, "555 Curtis St Auraria Denver, CO 80204"),
            ParkingLot("6", "Nutmeg Lot", 8.00, 4.0f, MSUDCampusLocation.NUTMEG_LOT, true, 70, 250, "1155 St Francis Way Northwestern Denver, CO 80204"),
            ParkingLot("7", "Boulder Creek", 8.00, 3.8f, MSUDCampusLocation.BOULDER_CREEK, true, 40, 120, "900 10th St Plaza Auraria Denver, CO 80204"),
            ParkingLot("8", "Elm Parking Lot", 8.00, 3.9f, MSUDCampusLocation.ELM_PARKING_LOT, true, 55, 180, "1301 5th St Auraria Denver, CO 80204"),
            ParkingLot("9", "7th Street Garage", 8.00, 4.1f, MSUDCampusLocation.SEVENTH_LOT, true, 65, 220, "777 Lawrence Way Auraria Denver, CO 80204"),
            ParkingLot("10", "5th Street Garage", 8.00, 4.1f, MSUDCampusLocation.FIFTH_STREET_GARAGE, true, 65, 220, "5th St Garage Auraria Denver, CO 80204"),
            ParkingLot("11", "Holly Lot", 8.00, 3.9f, MSUDCampusLocation.HOLLY_LOT, true, 70, 180, "Holly Lot Auraria Denver, CO 80204"),
            ParkingLot("12", "Juniper Lot", 8.00, 3.8f, MSUDCampusLocation.JUNIPER_LOT, true, 75, 150, "Juniper Lot Auraria Denver, CO 80204"),
            ParkingLot("13", "Maple Lot", 8.00, 3.7f, MSUDCampusLocation.MAPLE_LOT, true, 80, 140, "Maple Lot Auraria Denver, CO 80204"),
            ParkingLot("14", "Oak Lot", 8.00, 3.6f, MSUDCampusLocation.OAK_LOT, true, 60, 130, "Oak Lot Auraria Denver, CO 80204"),
            ParkingLot("15", "Aspen Lot", 8.00, 4.0f, MSUDCampusLocation.ASPEN_LOT, true, 85, 240, "Aspen Lot Auraria Denver, CO 80204"),
            ParkingLot("16", "Walnut Lot", 8.00, 4.2f, MSUDCampusLocation.WALNUT_LOT, true, 40, 160, "Walnut Lot Auraria Denver, CO 80204"),
            ParkingLot("17", "Beech Lot", 8.00, 3.5f, MSUDCampusLocation.BEECH_LOT, true, 50, 190, "Beech Lot Auraria Denver, CO 80204"),
            ParkingLot("18", "Birch Lot", 8.00, 3.6f, MSUDCampusLocation.BIRCH_LOT, true, 55, 170, "Birch Lot Auraria Denver, CO 80204")
        )

        // Setup parking lot spinner
        val parkingLotAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, parkingLots.map { it.name })
        parkingLotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        parkingLotSpinner.adapter = parkingLotAdapter

        // Setup category spinner
        val categories = arrayOf(getString(R.string.ease_of_finding_spot),
            getString(R.string.security), getString(R.string.cleanliness))
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        // Handle image upload
        uploadImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, imageRequestCode)
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
        val selectedParkingLotPosition = parkingLotSpinner.selectedItemPosition

        if (selectedParkingLotPosition == AdapterView.INVALID_POSITION) {
            Toast.makeText(this, getString(R.string.please_select_a_parking_lot), Toast.LENGTH_SHORT).show()
            return
        }

        val parkingLot = parkingLots[selectedParkingLotPosition]
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"

        if (rating > 0 && comment.isNotBlank()) {
            val review = Review(
                userId = userId,
                parkingLotId = parkingLot.id,
                rating = rating,
                comment = comment,
                timestamp = System.currentTimeMillis()
            )
            // Upload review to Firestore
            db.collection("reviews").add(review)
                .addOnSuccessListener {
                    Toast.makeText(this,
                        getString(R.string.review_submitted_successfully), Toast.LENGTH_SHORT).show()
                    returnToMaps()
                }
                .addOnFailureListener { e ->
                    Log.e("SubmitReviewActivity", getString(R.string.error_submitting_review), e)
                    Toast.makeText(this, "Error submitting review: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this,
                getString(R.string.please_provide_a_rating_and_comment), Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n", "DefaultLocale", "StringFormatMatches")
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
                averageRatingTextView.text =
                    getString(R.string.average_rating, String.format("%.1f", averageRating))
                reviewCountTextView.text = getString(R.string.total_reviews, reviewCount)
            }
            .addOnFailureListener { e ->
                Log.e("SubmitReviewActivity", getString(R.string.error_fetching_reviews), e)
            }
    }

    private fun returnToMaps() {
        // Create an intent to start the MapsActivity
        val intent = Intent(this, MapsActivity::class.java)

        // Optional: Add flags to clear the activity stack
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

        // Start the activity
        startActivity(intent)

        // Optional: Finish the current activity to remove it from the back stack
        finish()
    }
}