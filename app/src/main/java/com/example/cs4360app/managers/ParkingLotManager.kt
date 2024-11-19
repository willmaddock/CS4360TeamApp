package com.example.cs4360app.managers

import com.example.cs4360app.models.MSUDCampusLocation
import com.example.cs4360app.models.ParkingLot
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

object ParkingLotManager {

    private val parkingLots = listOf(
        ParkingLot("1", "Dogwood Parking Lot", 8.00, 4.5f, MSUDCampusLocation.DOGWOOD_PARKING_LOT, true, 90, 100, "7th St & Walnut Auraria Denver, CO 80204"),
        ParkingLot("2", "Tivoli Parking Garage", 8.00, 4.0f, MSUDCampusLocation.TIVOLI_PARKING_GARAGE, true, 30, 200, "901 Walnut St Auraria Denver, CO 80204"),
        ParkingLot("3", "Cherry Parking Lot", 6.50, 3.5f, MSUDCampusLocation.CHERRY_PARKING_LOT, true, 75, 150, "605 Walnut St Auraria Denver, CO 80204"),
        ParkingLot("4", "Spruce Parking Lot", 8.00, 3.0f, MSUDCampusLocation.SPRUCE_PARKING_LOT, true, 50, 300, "800 Walnut St Auraria Denver, CO 80204"),
        ParkingLot("5", "Fir Parking Lot", 6.50, 2.5f, MSUDCampusLocation.FIR_PARKING_LOT, true, 10, 400, "555 Curtis St Auraria Denver, CO 80204"),
        ParkingLot("6", "Nutmeg Lot", 8.00, 4.0f, MSUDCampusLocation.NUTMEG_LOT, true, 70, 250, "1155 St Francis Way Northwestern Denver, CO 80204"),
        ParkingLot("7", "Boulder Creek", 2.00, 3.8f, MSUDCampusLocation.BOULDER_CREEK, true, 40, 120, "900 10th St Plaza Auraria Denver, CO 80204"),
        ParkingLot("8", "Elm Parking Lot", 6.50, 3.9f, MSUDCampusLocation.ELM_PARKING_LOT, true, 55, 180, "1301 5th St Auraria Denver, CO 80204"),
        ParkingLot("9", "7th Street Garage", 8.00, 4.1f, MSUDCampusLocation.SEVENTH_LOT, true, 65, 220, "777 Lawrence Way Auraria Denver, CO 80204"),
        ParkingLot("10", "5th Street Garage", 6.50, 4.1f, MSUDCampusLocation.FIFTH_STREET_GARAGE, true, 65, 220, "5th St Garage Auraria Denver, CO 80204"),
        ParkingLot("11", "Holly Lot", 8.00, 3.9f, MSUDCampusLocation.HOLLY_LOT, true, 70, 180, "Holly Lot Auraria Denver, CO 80204"),
        ParkingLot("12", "Juniper Lot", 6.50, 3.8f, MSUDCampusLocation.JUNIPER_LOT, true, 75, 150, "Juniper Lot Auraria Denver, CO 80204"),
        ParkingLot("13", "Maple Lot", 8.00, 3.7f, MSUDCampusLocation.MAPLE_LOT, true, 80, 140, "Maple Lot Auraria Denver, CO 80204"),
        ParkingLot("14", "Oak Lot", 8.00, 3.6f, MSUDCampusLocation.OAK_LOT, true, 60, 130, "Oak Lot Auraria Denver, CO 80204"),
        ParkingLot("15", "Aspen Lot", 5.00, 4.0f, MSUDCampusLocation.ASPEN_LOT, true, 85, 240, "Aspen Lot Auraria Denver, CO 80204"),
        ParkingLot("16", "Walnut Lot", 5.00, 4.2f, MSUDCampusLocation.WALNUT_LOT, true, 40, 160, "Walnut Lot Auraria Denver, CO 80204"),
        ParkingLot("17", "Beech Lot", 5.00, 3.5f, MSUDCampusLocation.BEECH_LOT, true, 50, 190, "Beech Lot Auraria Denver, CO 80204"),
        ParkingLot("18", "Birch Lot", 5.00, 3.6f, MSUDCampusLocation.BIRCH_LOT, true, 55, 170, "Birch Lot Auraria Denver, CO 80204")
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
            MSUDCampusLocation.DOGWOOD_PARKING_LOT -> LatLng(39.744562552494635, -105.0083253825282)
            MSUDCampusLocation.TIVOLI_PARKING_GARAGE -> LatLng(39.74641815233135, -105.0061195427284)
            MSUDCampusLocation.CHERRY_PARKING_LOT -> LatLng(39.74411512001208, -105.00958392912607)
            MSUDCampusLocation.SPRUCE_PARKING_LOT -> LatLng(39.74461804779121, -105.00698112842873)
            MSUDCampusLocation.FIR_PARKING_LOT -> LatLng(39.74119220167536, -105.0087527236546)
            MSUDCampusLocation.NUTMEG_LOT -> LatLng(39.74226310246321, -105.00090275359969)
            MSUDCampusLocation.BOULDER_CREEK -> LatLng(39.74118605149313, -105.00285485547928)
            MSUDCampusLocation.ELM_PARKING_LOT -> LatLng(39.742554638969565, -105.00940650343364)
            MSUDCampusLocation.SEVENTH_LOT -> LatLng(39.7434473946466, -105.00752457926319)
            MSUDCampusLocation.FIFTH_STREET_GARAGE -> LatLng(39.74301082876547, -105.01033151701921)
            MSUDCampusLocation.HOLLY_LOT -> LatLng(39.74220914411681, -105.00579575657247)
            MSUDCampusLocation.JUNIPER_LOT -> LatLng(39.74077187408226, -105.00622955600369)
            MSUDCampusLocation.MAPLE_LOT -> LatLng(39.74191482504014, -105.00057102460512)
            MSUDCampusLocation.OAK_LOT -> LatLng(39.74239064023424, -105.0012472414017)
            MSUDCampusLocation.ASPEN_LOT -> LatLng(39.742527121045995, -105.01097830588711)
            MSUDCampusLocation.WALNUT_LOT -> LatLng(39.74245354173831, -105.01186504301093)
            MSUDCampusLocation.BEECH_LOT -> LatLng(39.740998809333234, -105.01002975687958)
            MSUDCampusLocation.BIRCH_LOT -> LatLng(39.74014093316553, -105.00923851541998)
            else -> null
        }
    }
}