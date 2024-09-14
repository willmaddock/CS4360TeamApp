package com.example.cs4360app.filter

import com.example.cs4360app.models.ParkingLot

object CostFilter {

    fun filterByCost(parkingLots: List<ParkingLot>, maxCost: Double, isMsudParkingLot: Boolean): List<ParkingLot> {
        return parkingLots.filter {
            it.cost <= maxCost && it.isMsudParkingLot == isMsudParkingLot
        }
    }
}