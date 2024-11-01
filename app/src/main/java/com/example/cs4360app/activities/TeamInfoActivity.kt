package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R

class TeamInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_info) // Ensure this matches your layout file name

        // Enable link clicking
        val linkTextView = findViewById<TextView>(R.id.linkTextView) // Ensure this ID matches
        linkTextView.movementMethod = LinkMovementMethod.getInstance()

        // Initialize the Acknowledge button
        val acknowledgeButton = findViewById<Button>(R.id.acknowledgeButton)
        acknowledgeButton.setOnClickListener {
            // Start the Maps Activity
            val intent = Intent(this, MapsActivity::class.java) // Replace with your Maps activity class
            startActivity(intent)
            finish() // Optional: Call finish() if you want to close this activity
        }
    }
}