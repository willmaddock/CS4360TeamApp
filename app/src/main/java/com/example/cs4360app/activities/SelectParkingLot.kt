package com.example.cs4360app.activities

import ParkingLotAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs4360app.databinding.ActivitySelectParkingLotBinding
import com.example.cs4360app.models.MSUDCampusLocation
import com.example.cs4360app.models.ParkingLot

class SelectParkingLot : AppCompatActivity() {

    private lateinit var binding: ActivitySelectParkingLotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectParkingLotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Assuming you have a list of ParkingLot objects
        val parkingLots: List<ParkingLot> = listOf(ParkingLot(
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
            )) // replace with your list

        // Assuming parkingLotRecyclerView is a RecyclerView in your layout
        binding.parkingLotRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.parkingLotRecyclerView.adapter = ParkingLotAdapter(parkingLots)

    }
}