package com.example.cs4360app.models

import android.os.Parcel
import android.os.Parcelable

// Enum for MSUD campus parking locations
enum class MSUDCampusLocation {
    DOGWOOD_PARKING_LOT,
    TIVOLI_PARKING_LOT,
    CHERRY_PARKING_LOT,
    SPRUCE_PARKING_LOT,
    FIR_PARKING_LOT,
    NUTMEG_LOT,
    BOULDER_CREEK,
    ELM_PARKING_LOT,
    SEVENTH_LOT
}

// Data class for parking lots
@Suppress("DEPRECATION")
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readFloat(),
        parcel.readSerializable() as? MSUDCampusLocation, // Cast to nullable
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeDouble(cost)
        parcel.writeFloat(rating)
        parcel.writeSerializable(location)  // Serializable type
        parcel.writeByte(if (isMsudParkingLot) 1 else 0)
        parcel.writeInt(availabilityPercentage)
        parcel.writeInt(proximity)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParkingLot> {
        override fun createFromParcel(parcel: Parcel): ParkingLot {
            return ParkingLot(parcel)
        }

        override fun newArray(size: Int): Array<ParkingLot?> {
            return arrayOfNulls(size)
        }
    }
}