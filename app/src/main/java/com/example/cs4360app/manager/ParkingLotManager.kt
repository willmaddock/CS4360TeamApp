package com.example.cs4360app.manager

import com.example.cs4360app.models.MSUDCampusLocation
import com.example.cs4360app.models.ParkingLot
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

object ParkingLotManager {

    // List of predefined parking lots with availability percentage, proximity in feet, and addresses
    private val parkingLots = listOf(
        ParkingLot("1", "Jordan Parking Garage", 10.0, 4.5f, MSUDCampusLocation.JORDAN_PARKING_GARAGE, true, 90, 100, "123 Jordan St"),
        ParkingLot("2", "Tivoli Parking Lot", 8.0, 4.0f, MSUDCampusLocation.TIVOLI_PARKING_LOT, true, 30, 200, "456 Tivoli St"),
        ParkingLot("3", "Auraria West", 5.0, 3.5f, MSUDCampusLocation.AURARIA_WEST, true, 75, 150, "789 Auraria W"),
        ParkingLot("4", "Auraria East", 7.0, 3.0f, MSUDCampusLocation.AURARIA_EAST, true, 50, 300, "321 Auraria E"),
        ParkingLot("5", "Ninth and Walnut", 6.0, 2.5f, MSUDCampusLocation.NINTH_AND_WALNUT, true, 10, 400, "654 Ninth St")
    )

    fun loadParkingLots(
        googleMap: GoogleMap,
        maxCost: Double,
        showPrice: Boolean = false,
        minAvailability: Int = 0,
        showAvailability: Boolean = false,
        showProximity: Boolean = false,
        showRating: Boolean = false,
        showAddress: Boolean = false
    ) {
        // Clear existing markers before loading new ones
        googleMap.clear()

        // Loop through each parking lot and check against filters
        parkingLots.forEach { lot ->
            if (lot.cost <= maxCost && lot.availabilityPercentage >= minAvailability) {
                getLatLngForLocation(lot.location)?.let { latLng ->
                    val markerColor = getMarkerColor(lot.availabilityPercentage)

                    // Build snippet based on selected filters
                    val snippet = buildSnippet(lot, showPrice, showAvailability, showProximity, showRating, showAddress)

                    // Add marker to the map
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
    }

    private fun buildSnippet(
        lot: ParkingLot,
        showPrice: Boolean,
        showAvailability: Boolean,
        showProximity: Boolean,
        showRating: Boolean,
        showAddress: Boolean
    ): String {
        return buildString {
            if (showPrice) append("Cost: \$${lot.cost} | ")
            if (showAvailability) append("Availability: ${lot.availabilityPercentage}% | ")
            if (showProximity) append("Proximity: ${lot.proximity} ft | ")
            if (showRating) append("Rating: ${lot.rating} | ")
            if (showAddress) append("Address: ${lot.address} | ")
        }.trimEnd(' ', '|')
            .takeIf { it.isNotEmpty() } ?: "No additional info"
    }

    private fun getLatLngForLocation(location: MSUDCampusLocation?): LatLng? {
        return when (location) {
            MSUDCampusLocation.JORDAN_PARKING_GARAGE -> LatLng(39.745473, -105.007460)
            MSUDCampusLocation.TIVOLI_PARKING_LOT -> LatLng(39.744338, -105.002847)
            MSUDCampusLocation.AURARIA_WEST -> LatLng(39.744556, -105.009145)
            MSUDCampusLocation.AURARIA_EAST -> LatLng(39.743115, -105.000376)
            MSUDCampusLocation.NINTH_AND_WALNUT -> LatLng(39.743883, -105.001878)
            null -> null // Return null for unknown location
        }
    }

    private fun getMarkerColor(availability: Int): Float {
        return when {
            availability >= 90 -> BitmapDescriptorFactory.HUE_RED // Full parking lot
            availability >= 75 -> BitmapDescriptorFactory.HUE_YELLOW // Almost full
            else -> BitmapDescriptorFactory.HUE_GREEN // Available
        }
    }
}