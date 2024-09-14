package com.example.cs4360app.models

// Enum for MSUD campus parking locations
enum class MSUDCampusLocation {
    JORDAN_PARKING_GARAGE,
    TIVOLI_PARKING_LOT,
    AURARIA_WEST,
    AURARIA_EAST,
    NINTH_AND_WALNUT
}

data class ParkingLot(
    val id: String,
    val name: String,
    val cost: Double,
    val rating: Float,
    val location: MSUDCampusLocation?,  // Nullable location
    val isMsudParkingLot: Boolean
)