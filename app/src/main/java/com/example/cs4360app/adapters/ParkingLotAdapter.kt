package com.example.cs4360app.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs4360app.R
import com.example.cs4360app.models.ParkingLot

class ParkingLotAdapter(
    private val parkingLots: List<ParkingLot>,
    private val onParkingLotClick: (ParkingLot) -> Unit // Click listener parameter
) : RecyclerView.Adapter<ParkingLotAdapter.ParkingLotViewHolder>() {

    // Inflate the item layout and create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingLotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_parking_lot, parent, false)
        return ParkingLotViewHolder(view)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ParkingLotViewHolder, position: Int) {
        val parkingLot = parkingLots[position]
        holder.bind(parkingLot)
        holder.itemView.setOnClickListener { onParkingLotClick(parkingLot) } // Set click listener
    }

    // Return the total number of items
    override fun getItemCount(): Int = parkingLots.size

    // ViewHolder class to hold item views
    class ParkingLotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.parking_lot_name)
        private val costTextView: TextView = itemView.findViewById(R.id.parking_lot_cost)
        private val ratingTextView: TextView = itemView.findViewById(R.id.parking_lot_rating)
        private val proximityTextView: TextView = itemView.findViewById(R.id.parking_lot_proximity)

        @SuppressLint("SetTextI18n", "DefaultLocale")
        fun bind(parkingLot: ParkingLot) {
            nameTextView.text = parkingLot.name
            costTextView.text = "$${parkingLot.cost}"
            ratingTextView.text = "Rating: ${parkingLot.rating}"

            // Display proximity in feet, convert to miles if greater than 1 mile
            val proximityInFeet = parkingLot.proximity
            proximityTextView.text = if (proximityInFeet < 5280) {
                "$proximityInFeet ft"
            } else {
                "${String.format("%.2f", proximityInFeet / 5280.0)} miles" // Convert to miles if greater than 1 mile
            }
        }
    }
}