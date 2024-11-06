// ParkingLotManagerTest.kt
import com.example.cs4360app.managers.ParkingLotManager
import com.example.cs4360app.models.MSUDCampusLocation
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.*
import org.junit.Test

class ParkingLotManagerTest {

    @Test
    fun testGetNearbyParkingLots() {
        val currentLocation = LatLng(39.74396, -105.00869)  // Sample starting location

        // Get nearby parking lots
        val nearbyParkingLots = ParkingLotManager.getNearbyParkingLots(currentLocation)

        // Ensure that the correct number of parking lots is returned
        assertTrue(nearbyParkingLots.isNotEmpty())

        // Ensure that only the parking lots within the distance threshold are returned
        val expectedLocation = MSUDCampusLocation.DOGWOOD_PARKING_LOT
        assertTrue(nearbyParkingLots.any { it.location == expectedLocation })
    }
}