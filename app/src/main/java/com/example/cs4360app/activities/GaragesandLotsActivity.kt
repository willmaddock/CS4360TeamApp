package com.example.cs4360app.activities

import android.os.Bundle
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R
import com.example.cs4360app.adapters.LocationExpandableAdapter
import com.example.cs4360app.activities.LocationData

class GaragesandLotsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garagesand_lots)

        // Find ExpandableListViews for garages and parking lots
        val expandableListGarages = findViewById<ExpandableListView>(R.id.expandable_list_garages)
        val expandableListParkingLots = findViewById<ExpandableListView>(R.id.expandable_list_parking_lots)

        // Data for garages
        val garageList = listOf(
            LocationData("Tivoli Garage", R.drawable.tivoli_garage, "$8.00", "Sheltered with convenient access to Auraria Parkway", "950 Walnut Street"),
            LocationData("7th Street Garage", R.drawable.seventh_street_garage, "$8.00", "Sheltered at the center of the main campus", "1399 8th Street"),
            LocationData("5th Street Garage", R.drawable.fifth_street_garage, "$6.50", "Sheltered with a high amount of spaces available", "1343 5th Street")
        )

// Data for parking lots in alphabetical order
        val lotList = listOf(
            LocationData("Aspen Lot", R.drawable.aspen_lot, "$5.00", "A well-priced lot near the Campus Village building", "1300 5th Street"),
            LocationData("Beech Lot", R.drawable.beech_lot, "$5.00", "A quiet lot, less crowded", "1010 5th Street"),
            LocationData("Birch Lot", R.drawable.birch_lot, "$5.00", "Near MSU sports facilities", "1601 W. Colfax Avenue"),
            LocationData("Cherry Lot", R.drawable.cherry_lot, "$6.50", "Less crowded, with easy access to I-25", "605 Walnut Street"),
            LocationData("Dogwood Lot", R.drawable.dogwood_lot, "$8.00", "Popular among students for the AES building", "799 Walnut Street"),
            LocationData("Elm Lot", R.drawable.elm_lot, "$6.50", "A large lot with many parking spaces", "1301 5th Street"),
            LocationData("Fir Lot", R.drawable.fir_lot, "$6.50", "Near the Administration building", "555 Curtis Street"),
            LocationData("Holly Lot", R.drawable.holly_lot, "$8.00", "Convenient lot with many spaces at the center of campus", "855 Curtis Street"),
            LocationData("Juniper Lot", R.drawable.juniper_lot, "$6.50", "Less traffic, quieter", "660 Curtis Street"),
            LocationData("Maple Lot", R.drawable.maple_lot, "$8.00", "A smaller lot near the Cherry Creek academic building", "921 Street Francis Way"),
            LocationData("Nutmeg Lot", R.drawable.nutmeg_lot, "$8.00", "A very limited lot mostly used by faculty", "1155 Street Francis Way"),
            LocationData("Oak Lot", R.drawable.oak_lot, "$8.00", "A very limited lot mostly used by faculty", "1025 Street Francis Way"),
            LocationData("Spruce Lot", R.drawable.spruce_lot, "$8.00", "Great lot at the center of the MSU campus", "800 Walnut Street"),
            LocationData("Walnut Lot", R.drawable.walnut_lot, "$5.00", "A great lot with great savings", "1333 4th Street")
        )



        // Set up adapters for garages and parking lots
        val garageAdapter = LocationExpandableAdapter(this, garageList)
        val lotAdapter = LocationExpandableAdapter(this, lotList)

        // Assign each adapter to its respective ExpandableListView
        expandableListGarages.setAdapter(garageAdapter)
        expandableListParkingLots.setAdapter(lotAdapter)
    }
}
