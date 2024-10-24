package com.example.cs4360app.activities

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cs4360app.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

class EventDetailsActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val venueId = intent.getStringExtra("VENUE_ID") ?: ""

        if (venueId.isNotEmpty()) {
            fetchVenueDetails(venueId)
        } else {
            Toast.makeText(this, R.string.no_venue_id_provided, Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchVenueDetails(venueId: String) {
        val apiKey = getString(R.string.ticket_master_key)
        val url = "https://app.ticketmaster.com/discovery/v2/venues/$venueId.json?apikey=$apiKey"

        lifecycleScope.launch {
            val response = makeApiRequest(url)
            response?.let {
                val venueInfo = parseVenueInfo(it)
                findViewById<TextView>(R.id.eventDetails).text = venueInfo
            } ?: run {
                Toast.makeText(this@EventDetailsActivity, R.string.error_fetching_data, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun makeApiRequest(url: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(url).build()
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    response.body?.string()
                } else {
                    val errorMessage = "Error ${response.code}: ${response.message}"
                    println(errorMessage)
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun parseVenueInfo(jsonResponse: String): String {
        val jsonObject = JSONObject(jsonResponse)

        val name = jsonObject.optString("name", "Unknown Venue")
        val city = jsonObject.optJSONObject("city")?.optString("name", "Unknown City") ?: "Unknown City"
        val state = jsonObject.optJSONObject("state")?.optString("name", "Unknown State") ?: "Unknown State"
        val country = jsonObject.optJSONObject("country")?.optString("name", "Unknown Country") ?: "Unknown Country"
        val parkingDetails = jsonObject.optString("parkingDetail", "No parking info available")

        val eventsObject = jsonObject.optJSONObject("upcomingEvents")
        val totalEvents = eventsObject?.optInt("_total", 0) ?: 0

        return """
        🏟️ **Venue**: $name
        🌍 **Location**: $city, $state, $country
        🚗 **Parking Info**: $parkingDetails
        🎟️ **Total Upcoming Events**: $totalEvents
    """.trimIndent()
    }
}