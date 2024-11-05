package com.example.cs4360app.models

// Enum for MSUD campus parking locations
enum class MSUDCampusLocation {
    DOGWOOD_PARKING_LOT,
    TIVOLI_PARKING_GARAGE,
    CHERRY_PARKING_LOT,
    SPRUCE_PARKING_LOT,
    FIR_PARKING_LOT,
    NUTMEG_LOT,
    BOULDER_CREEK,
    ELM_PARKING_LOT,
    SEVENTH_LOT,
    FIFTH_STREET_GARAGE,
    HOLLY_LOT,
    JUNIPER_LOT,
    MAPLE_LOT,
    OAK_LOT,
    ASPEN_LOT,
    WALNUT_LOT,
    BEECH_LOT,
    BIRCH_LOT
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