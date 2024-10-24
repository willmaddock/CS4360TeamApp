package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs4360app.R
import com.example.cs4360app.adapters.Notification
import com.example.cs4360app.adapters.NotificationsAdapter

class NotificationsActivity : AppCompatActivity() {
    private lateinit var notificationsRecyclerView: RecyclerView
    private lateinit var notificationsAdapter: NotificationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        notificationsRecyclerView = findViewById(R.id.notificationsRecyclerView)
        notificationsRecyclerView.layoutManager = LinearLayoutManager(this)

        val notifications = listOf(
            Notification(getString(R.string.parking_reminder), getString(R.string.don_t_forget_to_check_your_parking_time)),
            Notification(getString(R.string.event_alert), getString(R.string.new_events_available_in_your_area)),
            Notification(getString(R.string.system_update), getString(R.string.the_app_has_been_updated_to_version_1_1))
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}