package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs4360app.R
import com.example.cs4360app.adapters.Notification
import com.example.cs4360app.adapters.NotificationsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

class NotificationsActivity : AppCompatActivity() {
    private lateinit var notificationsRecyclerView: RecyclerView
    private lateinit var notificationsAdapter: NotificationsAdapter
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        notificationsRecyclerView = findViewById(R.id.notificationsRecyclerView)
        notificationsRecyclerView.layoutManager = LinearLayoutManager(this)

        val notifications = listOf(
            Notification(getString(R.string.parking_reminder), getString(R.string.don_t_forget_to_check_your_parking_time)),
            Notification(getString(R.string.event_alert), getString(R.string.new_events_available_in_your_area)),
            Notification(getString(R.string.system_update), getString(R.string.the_app_has_been_updated_to_version_1_1)),
            Notification(getString(R.string.team_event_alert), getString(R.string.upcoming_team_events)) // New team events notification
        )

        notificationsAdapter = NotificationsAdapter(notifications) { notification ->
            handleNotificationClick(notification)
        }

        notificationsRecyclerView.adapter = notificationsAdapter
    }

    private fun handleNotificationClick(notification: Notification) {
        val venueId = getSavedVenueId() // Retrieve saved venue ID from preferences or database

        when (notification.title) {
            getString(R.string.parking_reminder) -> {
                startActivity(Intent(this, ParkingDetailsActivity::class.java))
            }
            getString(R.string.event_alert) -> {
                val intent = Intent(this, EventDetailsActivity::class.java)
                intent.putExtra("EVENT_TITLE", notification.title)
                intent.putExtra("VENUE_ID", venueId) // Use the dynamic venue ID
                startActivity(intent)
            }
            getString(R.string.team_event_alert) -> {
                fetchTeamEvents() // Fetch events for the selected teams
            }
            getString(R.string.system_update) -> {
                showToast(getString(R.string.a_system_update_is_available))
            }
            else -> {
                showToast(getString(R.string.unknown_notification_clicked))
            }
        }
    }

    private fun getSavedVenueId(): String {
        return "KovZpZAFaJeA" // Use the correct venue ID for Ball Arena
    }

    private fun fetchTeamEvents() {
        lifecycleScope.launch {
            val nuggetsEvents = makeApiRequest("Denver Nuggets")
            val avalancheEvents = makeApiRequest("Colorado Avalanche")

            val combinedEvents = "Denver Nuggets Events:\n$nuggetsEvents\n\nColorado Avalanche Events:\n$avalancheEvents"
            showToast(combinedEvents)
        }
    }

    private suspend fun makeApiRequest(teamName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val apiKey = getString(R.string.ticket_master_key)
                val url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=$apiKey&keyword=$teamName"
                val request = Request.Builder().url(url).build()
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    parseEvents(response.body?.string())
                } else {
                    "Error fetching events for $teamName"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun parseEvents(response: String?): String {
        response?.let {
            val jsonObject = JSONObject(it)
            val eventsArray = jsonObject.optJSONObject("_embedded")?.optJSONArray("events")
            val events = StringBuilder()

            eventsArray?.let {
                for (i in 0 until it.length()) {
                    val event = it.getJSONObject(i)
                    val eventName = event.optString("name", "Unnamed Event")
                    val eventDate = event.optJSONObject("dates")?.optJSONObject("start")?.optString("localDate", "Unknown Date")
                    events.append("Event: $eventName on $eventDate\n")
                }
            } ?: return "No upcoming events found for this team."

            return events.toString()
        }
        return "No events data."
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}