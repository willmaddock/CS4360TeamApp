package com.example.cs4360app.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R

class EventDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        // Retrieve event details from the intent
        val eventTitle = intent.getStringExtra("EVENT_TITLE") ?: "No Title"
        val eventDate = intent.getStringExtra("EVENT_DATE") ?: "No Date"
        val eventLocation = intent.getStringExtra("EVENT_LOCATION") ?: "No Location"
        val eventDescription = intent.getStringExtra("EVENT_DESCRIPTION") ?: "No Description"

        // Set the values in the TextViews
        findViewById<TextView>(R.id.eventTitle).text = eventTitle
        findViewById<TextView>(R.id.eventDetails).text = eventDescription // Changed to eventDescription

        // Set up the register button click listener
        findViewById<Button>(R.id.registerButton).setOnClickListener {
            registerForEvent(eventTitle)
        }
    }

    private fun registerForEvent(eventTitle: String) {
        // Implement registration logic (e.g., show a confirmation message)
        Toast.makeText(this, "Registered for $eventTitle", Toast.LENGTH_SHORT).show()
    }
}