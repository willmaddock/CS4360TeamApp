package com.example.cs4360app.models

// Enum for MSUD campus parking locations
enum class MSUDCampusLocation {
    JORDAN_PARKING_GARAGE,
    TIVOLI_PARKING_LOT,
    AURARIA_WEST,
    AURARIA_EAST,
    NINTH_AND_WALNUT
}

// Data class for parking lots
data class ParkingLot(
    val id: String,
    val name: String,
    val cost: Double,
    val rating: Float,
    val location: MSUDCampusLocation?,  // Nullable location
    val isMsudParkingLot: Boolean,
    val availabilityPercentage: Int,     // Field for availability percentage
    val proximity: Int,                   // Field for proximity in feet
    val address: String                    // New field for street address
)