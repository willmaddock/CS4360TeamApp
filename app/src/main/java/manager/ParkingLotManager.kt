package manager

import com.example.cs4360app.models.MSUDCampusLocation
import com.example.cs4360app.models.ParkingLot
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

object ParkingLotManager {

    // List of predefined parking lots
    private val parkingLots = listOf(
        ParkingLot("1", "Jordan Parking Garage", 10.0, 4.5f, MSUDCampusLocation.JORDAN_PARKING_GARAGE, true),
        ParkingLot("2", "Tivoli Parking Lot", 8.0, 4.0f, MSUDCampusLocation.TIVOLI_PARKING_LOT, true),
        ParkingLot("3", "Auraria West", 5.0, 3.5f, MSUDCampusLocation.AURARIA_WEST, true),
        ParkingLot("4", "Auraria East", 7.0, 3.0f, MSUDCampusLocation.AURARIA_EAST, true),
        ParkingLot("5", "Ninth and Walnut", 6.0, 2.5f, MSUDCampusLocation.NINTH_AND_WALNUT, true)
    )

    // Load parking lots onto the map based on the specified maximum cost
    fun loadParkingLots(googleMap: GoogleMap, maxCost: Double) {
        // Clear existing markers before loading new ones
        googleMap.clear()

        for (lot in parkingLots) {
            if (lot.cost <= maxCost) {
                val latLng = getLatLngForLocation(lot.location)
                latLng?.let {
                    googleMap.addMarker(MarkerOptions().position(it).title(lot.name))
                }
            }
        }
    }

    // Get LatLng based on the MSUDCampusLocation
    fun getLatLngForLocation(location: MSUDCampusLocation?): LatLng? {
        return when (location) {
            MSUDCampusLocation.JORDAN_PARKING_GARAGE -> LatLng(39.745473, -105.007460)
            MSUDCampusLocation.TIVOLI_PARKING_LOT -> LatLng(39.744338, -105.002847)
            MSUDCampusLocation.AURARIA_WEST -> LatLng(39.744556, -105.009145)
            MSUDCampusLocation.AURARIA_EAST -> LatLng(39.743115, -105.000376)
            MSUDCampusLocation.NINTH_AND_WALNUT -> LatLng(39.743883, -105.001878)
            null -> null // Return null for unknown location
        }
    }
}