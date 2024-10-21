@file:Suppress("SameReturnValue")

package com.example.cs4360app.activities

import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R
import com.example.cs4360app.databinding.ActivitySurveyBinding
import com.google.firebase.firestore.FirebaseFirestore

class SurveyActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySurveyBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmitSurvey.setOnClickListener {
            if (validateInputs()) {
                submitSurveyResponse()
            }
        }
    }

    private fun validateInputs(): Boolean {
        // Remove or comment out the validation for the biggest concern field
        // This means no validation is applied to the biggest concern field.

        // You can add validation for other fields if needed
        // Example:
        // if (binding.roleSpinner.selectedItem == null) {
        //     Toast.makeText(this, "Please select a role.", Toast.LENGTH_SHORT).show()
        //     return false
        // }

        return true
    }

    private fun submitSurveyResponse() {
        val responseId = firestore.collection("surveys")
            .document("customerEngagement")
            .collection("responses").document().id

        val surveyData = SurveyData(
            role = binding.roleSpinner.selectedItem.toString(),
            topPriorities = getSelectedItems(
                R.id.checkboxRealTimeUpdates,
                R.id.checkboxAccurateNavigation,
                R.id.checkboxNotifications,
                R.id.checkboxEventIntegration,
                R.id.checkboxReservationSystem,
                R.id.checkboxUserReviews,
                R.id.checkboxPaymentOptions,
                R.id.checkboxParkingLotFeatures
            ),
            realTimeUpdatesImportance = binding.realTimeUpdatesImportanceSpinner.selectedItem.toString(),
            reserveParkingValue = binding.reserveParkingValueSpinner.selectedItem.toString(),
            eventIntegrationImportance = binding.eventIntegrationImportanceSpinner.selectedItem.toString(),
            featurePreferences = getSelectedItems(
                R.id.checkboxRealTimeUpdates,
                R.id.checkboxAccurateNavigation,
                R.id.checkboxNotifications,
                R.id.checkboxEventIntegration,
                R.id.checkboxReservationSystem,
                R.id.checkboxUserReviews,
                R.id.checkboxPaymentOptions,
                R.id.checkboxParkingLotFeatures
            ),
            userReviewsImportance = binding.userReviewsImportanceSpinner.selectedItem.toString(),
            findCarFeatureInterest = binding.findCarFeatureInterestSpinner.selectedItem.toString(),
            appUsability = binding.appUsabilitySpinner.selectedItem.toString(),
            customerSupportImportance = binding.customerSupportImportanceSpinner.selectedItem.toString(),
            biggestConcern = binding.biggestConcernEditText.text.toString(),
            additionalFeatures = binding.additionalFeaturesEditText.text.toString(),
            otherComments = binding.otherCommentsEditText.text.toString()
        )

        firestore.collection("surveys")
            .document("customerEngagement")
            .collection("responses")
            .document(responseId)
            .set(surveyData)
            .addOnSuccessListener {
                Toast.makeText(this, "Survey submitted successfully!", Toast.LENGTH_SHORT).show()
                finish() // Close the survey screen
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error submitting survey: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getSelectedItems(vararg checkboxIds: Int): List<String> {
        val selectedItems = mutableListOf<String>()
        for (checkboxId in checkboxIds) {
            val checkbox = findViewById<CheckBox>(checkboxId)
            if (checkbox.isChecked) {
                selectedItems.add(checkbox.text.toString())
            }
        }
        return selectedItems
    }
}

// Data class for survey data
data class SurveyData(
    val role: String,
    val topPriorities: List<String>,
    val realTimeUpdatesImportance: String,
    val reserveParkingValue: String,
    val eventIntegrationImportance: String,
    val featurePreferences: List<String>,
    val userReviewsImportance: String,
    val findCarFeatureInterest: String,
    val appUsability: String,
    val customerSupportImportance: String,
    val biggestConcern: String,
    val additionalFeatures: String,
    val otherComments: String
)