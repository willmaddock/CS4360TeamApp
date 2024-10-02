package com.example.cs4360app.managers

import com.example.cs4360app.models.MSUDCampusLocation
import com.example.cs4360app.models.ParkingLot
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

object ParkingLotManager {

    private val parkingLots = listOf(
        ParkingLot("1", "Jordan Parking Garage", 10.0, 4.5f, MSUDCampusLocation.JORDAN_PARKING_GARAGE, true, 90, 100, "123 Jordan St"),
        ParkingLot("2", "Tivoli Parking Lot", 8.0, 4.0f, MSUDCampusLocation.TIVOLI_PARKING_LOT, true, 30, 200, "456 Tivoli St"),
        ParkingLot("3", "Auraria West", 5.0, 3.5f, MSUDCampusLocation.AURARIA_WEST, true, 75, 150, "789 Auraria W"),
        ParkingLot("4", "Auraria East", 7.0, 3.0f, MSUDCampusLocation.AURARIA_EAST, true, 50, 300, "321 Auraria E"),
        ParkingLot("5", "Ninth and Walnut", 6.0, 2.5f, MSUDCampusLocation.NINTH_AND_WALNUT, true, 10, 400, "654 Ninth St")
    )

    fun loadParkingLots(
        googleMap: GoogleMap,
        showPrice: Boolean = false,
        showAvailability: Boolean = false,
        showProximity: Boolean = false,
        showRating: Boolean = false,
        showAddress: Boolean = false
    ) {
        googleMap.clear()

        parkingLots.forEach { lot ->
            getLatLngForLocation(lot.location)?.let { latLng ->
                val (markerColor, availabilityKeyword) = getMarkerColorAndKeyword(lot.availabilityPercentage)
                val snippet = buildSnippet(lot, availabilityKeyword, showPrice, showAvailability, showProximity, showRating, showAddress)

                googleMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(lot.name)
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                )
            }
        }
    }

    private fun buildSnippet(
        lot: ParkingLot,
        availabilityKeyword: String,
        showPrice: Boolean,
        showAvailability: Boolean,
        showProximity: Boolean,
        showRating: Boolean,
        showAddress: Boolean
    ): String {
        return buildString {
            if (showPrice) append("Cost: \$${lot.cost} | ")
            if (showAvailability) append("Availability: $availabilityKeyword (${lot.availabilityPercentage}%) | ")
            if (showProximity) append("Proximity: ${lot.proximity} ft | ")
            if (showRating) append("Rating: ${lot.rating} | ")
            if (showAddress) append("Address: ${lot.address} | ")
        }.trimEnd(' ', '|')
            .takeIf { it.isNotEmpty() } ?: "No additional info"
    }

    private fun getMarkerColorAndKeyword(availability: Int): Pair<Float, String> {
        return when {
            availability >= 90 -> Pair(BitmapDescriptorFactory.HUE_RED, "Full")
            availability >= 75 -> Pair(BitmapDescriptorFactory.HUE_YELLOW, "Almost Full")
            else -> Pair(BitmapDescriptorFactory.HUE_GREEN, "Available")
        }
    }

    private fun getLatLngForLocation(location: MSUDCampusLocation?): LatLng? {
        return when (location) {
            MSUDCampusLocation.JORDAN_PARKING_GARAGE -> LatLng(39.745473, -105.007460)
            MSUDCampusLocation.TIVOLI_PARKING_LOT -> LatLng(39.744338, -105.002847)
            MSUDCampusLocation.AURARIA_WEST -> LatLng(39.744556, -105.009145)
            MSUDCampusLocation.AURARIA_EAST -> LatLng(39.743115, -105.000376)
            MSUDCampusLocation.NINTH_AND_WALNUT -> LatLng(39.743883, -105.001878)
            null -> null
        }
    }
}