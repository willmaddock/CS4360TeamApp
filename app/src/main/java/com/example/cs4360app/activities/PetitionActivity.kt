package com.example.cs4360app.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R
import com.example.cs4360app.models.Petition
import com.google.firebase.firestore.FirebaseFirestore

class PetitionActivity : AppCompatActivity() {

    private lateinit var petitionDescription: EditText
    private lateinit var gracePeriodSuggestion: EditText
    private lateinit var submitPetitionButton: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petition)

        // Initialize UI elements
        petitionDescription = findViewById(R.id.petitionDescription)
        gracePeriodSuggestion = findViewById(R.id.gracePeriodSuggestion)
        submitPetitionButton = findViewById(R.id.submitPetitionButton)

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance() // Firestore Database

        // Set up the submit button click listener
        submitPetitionButton.setOnClickListener {
            val description = petitionDescription.text.toString().trim()
            val gracePeriod = gracePeriodSuggestion.text.toString().trim()

            // Validate input
            if (description.isBlank()) {
                Toast.makeText(this,
                    getString(R.string.please_describe_your_petition), Toast.LENGTH_SHORT).show()
            } else {
                submitPetition(description, gracePeriod)
            }
        }
    }

    // Function to submit the petition to Firestore
    private fun submitPetition(description: String, gracePeriod: String?) {
        val petition = Petition(
            userId = "anonymous",  // Set userId as anonymous since no login
            description = description,
            gracePeriod = gracePeriod,
            timestamp = System.currentTimeMillis()
        )

        // Add petition to Firestore collection
        db.collection("petitions")
            .add(petition)
            .addOnSuccessListener {
                Toast.makeText(this,
                    getString(R.string.petition_submitted_successfully), Toast.LENGTH_SHORT).show()
                finish() // Close activity on success
            }
            .addOnFailureListener { e ->
                Toast.makeText(this,
                    getString(R.string.failed_to_submit_petition, e.message), Toast.LENGTH_LONG).show()
            }
    }
}