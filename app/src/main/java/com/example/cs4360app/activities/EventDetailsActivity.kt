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
        val eventTitle = intent.getStringExtra("EVENT_TITLE") ?: getString(R.string.no_title)
        intent.getStringExtra("EVENT_DATE") ?: getString(R.string.no_date)
        intent.getStringExtra("EVENT_LOCATION") ?: getString(R.string.no_location)
        val eventDescription = intent.getStringExtra("EVENT_DESCRIPTION") ?: getString(R.string.no_description)

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
        Toast.makeText(this, getString(R.string.registered_for, eventTitle), Toast.LENGTH_SHORT).show()
    }
}