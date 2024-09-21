package com.example.cs4360app.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R
import com.example.cs4360app.models.Petition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PetitionActivity : AppCompatActivity() {

    private lateinit var petitionDescription: EditText
    private lateinit var gracePeriodSuggestion: EditText
    private lateinit var submitPetitionButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petition)

        petitionDescription = findViewById(R.id.petitionDescription)
        gracePeriodSuggestion = findViewById(R.id.gracePeriodSuggestion)
        submitPetitionButton = findViewById(R.id.submitPetitionButton)

        auth = FirebaseAuth.getInstance() // Firebase Authentication
        db = FirebaseFirestore.getInstance() // Firestore Database

        // Check if the user is logged in
        if (auth.currentUser == null) {
            Toast.makeText(this, "Please log in to submit a petition.", Toast.LENGTH_LONG).show()
            finish() // Redirect if user is not authenticated
        }

        submitPetitionButton.setOnClickListener {
            val description = petitionDescription.text.toString().trim()
            val gracePeriod = gracePeriodSuggestion.text.toString().trim()

            if (description.isBlank()) {
                Toast.makeText(this, "Please describe your petition.", Toast.LENGTH_SHORT).show()
            } else {
                submitPetition(description, gracePeriod)
            }
        }
    }

    private fun submitPetition(description: String, gracePeriod: String?) {
        val userId = auth.currentUser?.uid ?: return
        val petition = Petition(
            userId = userId,
            description = description,
            gracePeriod = gracePeriod,
            timestamp = System.currentTimeMillis()
        )

        db.collection("petitions")
            .add(petition)
            .addOnSuccessListener {
                Toast.makeText(this, "Petition submitted successfully.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to submit petition: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}