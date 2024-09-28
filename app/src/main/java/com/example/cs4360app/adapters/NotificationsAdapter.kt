package com.example.cs4360app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs4360app.R

// Data class representing a Notification
data class Notification(val title: String, val message: String)

// Adapter for displaying notifications in a RecyclerView
class NotificationsAdapter(
    private val notifications: List<Notification>,
    private val onItemClick: (Notification) -> Unit // Add click listener
) : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    // ViewHolder for each notification item
    class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.notificationTitle)
        val messageTextView: TextView = view.findViewById(R.id.notificationMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.titleTextView.text = notification.title
        holder.messageTextView.text = notification.message

        // Set click listener for the entire item
        holder.itemView.setOnClickListener {
            onItemClick(notification)
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}