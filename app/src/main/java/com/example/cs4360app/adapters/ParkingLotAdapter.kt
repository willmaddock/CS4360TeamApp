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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingLotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_parking_lot, parent, false)
        return ParkingLotViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParkingLotViewHolder, position: Int) {
        val parkingLot = parkingLots[position]
        holder.bind(parkingLot)
        holder.itemView.setOnClickListener { onParkingLotClick(parkingLot) } // Set click listener
    }

    override fun getItemCount(): Int = parkingLots.size

    class ParkingLotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.parking_lot_name)
        private val costTextView: TextView = itemView.findViewById(R.id.parking_lot_cost)
        private val ratingTextView: TextView = itemView.findViewById(R.id.parking_lot_rating)

        @SuppressLint("SetTextI18n")
        fun bind(parkingLot: ParkingLot) {
            nameTextView.text = parkingLot.name
            costTextView.text = "$${parkingLot.cost}"
            ratingTextView.text = "Rating: ${parkingLot.rating}"
        }
    }
}