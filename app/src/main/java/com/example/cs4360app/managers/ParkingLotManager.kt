package com.example.cs4360app.managers

import com.example.cs4360app.models.MSUDCampusLocation
import com.example.cs4360app.models.ParkingLot
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

object ParkingLotManager {

    private val parkingLots = listOf(
        ParkingLot("1", "Dogwood Parking Lot", 7.25, 4.5f, MSUDCampusLocation.DOGWOOD_PARKING_LOT, true, 90, 100, "7th St & Walnut Auraria Denver, CO 80204"),
        ParkingLot("2", "Tivoli Parking Lot", 7.25, 4.0f, MSUDCampusLocation.TIVOLI_PARKING_LOT, true, 30, 200, "901 Walnut St Auraria Denver, CO 80204"),
        ParkingLot("3", "Cherry Parking Lot", 5.75, 3.5f, MSUDCampusLocation.CHERRY_PARKING_LOT, true, 75, 150, "605 Walnut St Auraria Denver, CO 80204"),
        ParkingLot("4", "Spruce Parking Lot", 7.25, 3.0f, MSUDCampusLocation.SPRUCE_PARKING_LOT, true, 50, 300, "800 Walnut St Auraria Denver, CO 80204"),
        ParkingLot("5", "Fir Parking Lot", 5.75, 2.5f, MSUDCampusLocation.FIR_PARKING_LOT, true, 10, 400, "555 Curtis St Auraria Denver, CO 80204"),
        ParkingLot("6", "Nutmeg Lot", 7.25, 4.0f, MSUDCampusLocation.NUTMEG_LOT, true, 70, 250, "1155 St Francis Way Northwestern Denver, CO 80204"),
        ParkingLot("7", "Boulder Creek", 2.00, 3.8f, MSUDCampusLocation.BOULDER_CREEK, true, 40, 120, "900 10th St Plaza Auraria Denver, CO 80204"),
        ParkingLot("8", "Elm Parking Lot", 5.75, 3.9f, MSUDCampusLocation.ELM_PARKING_LOT, true, 55, 180, "1301 5th St Auraria Denver, CO 80204"),
        ParkingLot("9", "7th Street Garage", 7.25, 4.1f, MSUDCampusLocation.SEVENTH_LOT, true, 65, 220, "777 Lawrence Way Auraria Denver, CO 80204")
    )

    fun getNearbyParkingLots(currentLocation: LatLng): List<ParkingLot> {
        return parkingLots.filter { lot ->
            val lotLocation = getLatLngForLocation(lot.location)
            lotLocation != null && calculateDistance(currentLocation, lotLocation) <= 1000 // 1000 meters as an example
        }
    }

    private fun calculateDistance(start: LatLng, end: LatLng): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, results)
        return results[0] // returns distance in meters
    }

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
            availability >= 80 -> Pair(BitmapDescriptorFactory.HUE_RED, "Full")
            availability >= 60 -> Pair(BitmapDescriptorFactory.HUE_YELLOW, "Almost Full")
            else -> Pair(BitmapDescriptorFactory.HUE_GREEN, "Available")
        }
    }

    private fun getLatLngForLocation(location: MSUDCampusLocation?): LatLng? {
        return when (location) {
            MSUDCampusLocation.DOGWOOD_PARKING_LOT -> LatLng(39.74396, -105.00869)
            MSUDCampusLocation.TIVOLI_PARKING_LOT -> LatLng(39.7459, -105.00609)
            MSUDCampusLocation.CHERRY_PARKING_LOT -> LatLng(39.74378, -105.01021)
            MSUDCampusLocation.SPRUCE_PARKING_LOT -> LatLng(39.74407, -105.00842)
            MSUDCampusLocation.FIR_PARKING_LOT -> LatLng(39.74083, -105.00909)
            MSUDCampusLocation.NUTMEG_LOT -> LatLng(39.74227, -105.00056)
            MSUDCampusLocation.BOULDER_CREEK -> LatLng(39.740945, -105.003022)  // Example coordinates
            MSUDCampusLocation.ELM_PARKING_LOT -> LatLng(39.74255, -105.0106)  // Example coordinates
            MSUDCampusLocation.SEVENTH_LOT -> LatLng(39.74312, -105.00587)  // Example coordinates
            null -> null
        }
    }
}