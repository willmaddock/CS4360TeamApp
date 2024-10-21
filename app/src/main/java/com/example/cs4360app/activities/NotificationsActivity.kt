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

        // Example notifications (replace with real data)
        val notifications = listOf(
            Notification(
                getString(R.string.parking_reminder),
                getString(R.string.don_t_forget_to_check_your_parking_time)),
            Notification(getString(R.string.event_alert), getString(R.string.new_events_available_in_your_area)),
            Notification(
                getString(R.string.system_update),
                getString(R.string.the_app_has_been_updated_to_version_1_1))
        )

        // Initialize the adapter with a click listener
        notificationsAdapter = NotificationsAdapter(notifications) { notification ->
            // Handle the click event
            handleNotificationClick(notification)
        }

        notificationsRecyclerView.adapter = notificationsAdapter
    }

    private fun handleNotificationClick(notification: Notification) {
        // Perform an action based on the clicked notification
        when (notification.title) {
            "Parking Reminder" -> {
                // Start ParkingDetailsActivity
                val intent = Intent(this, ParkingDetailsActivity::class.java)
                startActivity(intent)
            }

            "Event Alert" -> {
                // Show event details or open an event activity
                val intent = Intent(this, EventDetailsActivity::class.java)
                startActivity(intent)
            }

            "System Update" -> {
                // Show update information
                showToast(getString(R.string.a_system_update_is_available))
            }

            else -> {
                // Handle other cases or do nothing
                showToast(getString(R.string.unknown_notification_clicked))
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}