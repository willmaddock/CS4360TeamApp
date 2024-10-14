package com.example.cs4360app.activities

import android.widget.TextView
import com.example.cs4360app.R
import com.example.cs4360app.models.ParkingLot
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ParkingDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_details)

        val parkingLot = intent.getParcelableExtra<ParkingLot>("parkingLot")

        findViewById<TextView>(R.id.parkingName).text = parkingLot?.name
        findViewById<TextView>(R.id.parkingCost).text = parkingLot?.cost
        findViewById<TextView>(R.id.parkingRating).text = parkingLot?.rating.toString()
        findViewById<TextView>(R.id.parkingProximity).text
    }
}