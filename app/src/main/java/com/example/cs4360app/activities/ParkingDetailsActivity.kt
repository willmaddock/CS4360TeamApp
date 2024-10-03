package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R

class ParkingDetailsActivity : AppCompatActivity() {
    //private lateinit var backToMapsButton2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_details)
        //backToMapsButton2 = findViewById(R.id.back_to_maps_button2)

        val sharedPreferences = getSharedPreferences("parking_info", MODE_PRIVATE)
        val parkingLotName = sharedPreferences.getString("PARKING_LOT_NAME", "")
        val parkingLotCost = Double.fromBits(sharedPreferences.getLong("PARKING_LOT_COST", 0L)) // Retrieve as Double
        val parkingLotProximity = sharedPreferences.getInt("PARKING_LOT_PROXIMITY", 0)
        val parkingLotAddress = sharedPreferences.getString("PARKING_LOT_ADDRESS", "")
        val parkingLotRating = sharedPreferences.getFloat("PARKING_LOT_RATING", 0.0f)


        val parkingLotInfoTextView1: TextView = findViewById(R.id.parking_lot_name2)
        parkingLotInfoTextView1.text = "Parking Lot: $parkingLotName"

        val parkingLotInfoTextView2: TextView = findViewById(R.id.parking_lot_cost2)
        parkingLotInfoTextView2.text = "Price: $$parkingLotCost"

        val parkingLotInfoTextView3: TextView = findViewById(R.id.parking_lot_proximity2)
        parkingLotInfoTextView3.text = "Parking Lot Distance: $parkingLotProximity" +"ft"

        val parkingLotInfoTextView4: TextView = findViewById(R.id.parking_lot_address2)
        parkingLotInfoTextView4.text = "Address: $parkingLotAddress"

        val parkingLotInfoTextView5: TextView = findViewById(R.id.parking_lot_rating2)
        parkingLotInfoTextView5.text = "Rating: $parkingLotRating"

        val backToMapsButton2: Button = findViewById(R.id.back_to_maps_button2)
        backToMapsButton2.setOnClickListener {
            navigateToMaps()
        }

        // TODO: Implement your parking details display logic here
        // For example, retrieve extras from the intent, set up UI elements, etc.
    }

    private fun navigateToMaps() {
        startActivity(Intent(this, MapsActivity::class.java))
        finish()
    }

}