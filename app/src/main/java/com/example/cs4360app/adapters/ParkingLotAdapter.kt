package com.example.cs4360app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs4360app.R
import com.example.cs4360app.models.ParkingLot

class ParkingLotAdapter(private var parkingLots: List<ParkingLot>) : RecyclerView.Adapter<ParkingLotAdapter.ParkingLotViewHolder>() {

    // ViewHolder class to represent each ParkingLot item
    class ParkingLotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lotNameTextView: TextView = itemView.findViewById(R.id.lotNameTextView)
        val lotCostTextView: TextView = itemView.findViewById(R.id.lotCostTextView)
        val lotRatingTextView: TextView = itemView.findViewById(R.id.lotRatingTextView)

        fun bind(parkingLot: ParkingLot) {
            lotNameTextView.text = parkingLot.name
            lotCostTextView.text = "Cost: $${parkingLot.cost}"
            lotRatingTextView.text = "Rating: ${parkingLot.rating}"
        }
    }

    // Creates new views for RecyclerView items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingLotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_parking_lot, parent, false)
        return ParkingLotViewHolder(view)
    }

    // Binds data to the view holder for each item
    override fun onBindViewHolder(holder: ParkingLotViewHolder, position: Int) {
        holder.bind(parkingLots[position])
    }

    // Returns the total number of items in the data set
    override fun getItemCount(): Int = parkingLots.size

    // Updates the data set for the adapter and refreshes the RecyclerView
    fun updateData(newParkingLots: List<ParkingLot>) {
        this.parkingLots = newParkingLots
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}