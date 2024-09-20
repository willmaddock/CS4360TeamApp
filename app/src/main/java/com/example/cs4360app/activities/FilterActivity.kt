package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R
import com.example.cs4360app.models.ParkingLot
import com.example.cs4360app.models.MSUDCampusLocation

class FilterActivity : AppCompatActivity() {

    private var seekBar: SeekBar? = null
    private var selectedCostText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_activity_filter_cost)

        seekBar = findViewById(R.id.costSeekBar)
        selectedCostText = findViewById(R.id.selectedCostText)

        // Example parking lot list using MSUDCampusLocation
        val parkingLots = listOf(
            ParkingLot(
                id = "1",
                name = "Jordan Parking Garage",
                cost = 10.0,
                rating = 4.2f,
                location = MSUDCampusLocation.JORDAN_PARKING_GARAGE,
                isMsudParkingLot = true
            ),
            ParkingLot(
                id = "2",
                name = "Tivoli Parking Lot",
                cost = 8.0,
                rating = 3.8f,
                location = MSUDCampusLocation.TIVOLI_PARKING_LOT,
                isMsudParkingLot = true
            ),
            ParkingLot(
                id = "3",
                name = "Auraria West",
                cost = 5.0,
                rating = 4.0f,
                location = MSUDCampusLocation.AURARIA_WEST,
                isMsudParkingLot = true
            ),
            ParkingLot(
                id = "4",
                name = "9th and Walnut",
                cost = 6.0,
                rating = 4.5f,
                location = MSUDCampusLocation.NINTH_AND_WALNUT,
                isMsudParkingLot = true
            ),
            ParkingLot(
                id = "5",
                name = "Auraria East",
                cost = 7.0,
                rating = 3.5f,
                location = MSUDCampusLocation.AURARIA_EAST,
                isMsudParkingLot = true
            )
        )

        // Example usage with nullable location
        val otherParkingLot = ParkingLot(
            id = "6",
            name = "Other Parking Lot",
            cost = 4.0,
            rating = 4.3f,
            location = null,  // Location can be null
            isMsudParkingLot = false
        )

        // Update SeekBar and display the cost in $0.50 increments
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Convert the progress value to a cost with $0.50 increments
                val maxCost = progress * 0.5
                selectedCostText?.text = "Max Cost: $$maxCost"

                // Pass the filter data (max cost) to MapsActivity
                val intent = Intent(this@FilterActivity, MapsActivity::class.java)
                intent.putExtra("maxCost", maxCost)
                startActivity(intent)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}