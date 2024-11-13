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
            LocationData(getString(R.string.tivoli_garage), R.drawable.tivoli_garage, "$8.00",
                getString(
                    R.string.sheltered_with_convenient_access_to_auraria_parkway
                ), getString(R.string._950_walnut_street)),
            LocationData("7th Street Garage", R.drawable.seventh_street_garage, "$8.00",
                getString(R.string.sheltered_at_the_center_of_the_main_campus),
                getString(R.string._1399_8th_street)),
            LocationData(getString(R.string._5th_street_garage), R.drawable.fifth_street_garage, "$6.50",
                getString(
                    R.string.sheltered_with_a_high_amount_of_spaces_available
                ),
                getString(R.string._1343_5th_street))
        )

// Data for parking lots in alphabetical order
        val lotList = listOf(
            LocationData(getString(R.string.aspen_lot), R.drawable.aspen_lot, "$5.00",
                getString(R.string.a_well_priced_lot_near_the_campus_village_building),
                getString(R.string._1300_5th_street)),
            LocationData(getString(R.string.beech_lot), R.drawable.beech_lot, "$5.00",
                getString(R.string.a_quiet_lot_less_crowded), getString(R.string._1010_5th_street)),
            LocationData(getString(R.string.birch_lot), R.drawable.birch_lot, "$5.00",
                getString(R.string.near_msu_sports_facilities),
                getString(R.string._1601_w_colfax_avenue)),
            LocationData(getString(R.string.cherry_lot), R.drawable.cherry_lot, "$6.50",
                getString(R.string.less_crowded_with_easy_access_to_i_25),
                getString(R.string._605_walnut_street)),
            LocationData(getString(R.string.dogwood_lot), R.drawable.dogwood_lot, "$8.00",
                getString(
                    R.string.popular_among_students_for_the_aes_building
                ), getString(R.string._799_walnut_street)),
            LocationData(getString(R.string.elm_lot), R.drawable.elm_lot, "$6.50",
                getString(R.string.a_large_lot_with_many_parking_spaces),
                getString(R.string._1301_5th_street)),
            LocationData(getString(R.string.fir_lot), R.drawable.fir_lot, "$6.50",
                getString(R.string.near_the_administration_building),
                getString(R.string._555_curtis_street)),
            LocationData(getString(R.string.holly_lot), R.drawable.holly_lot, "$8.00",
                getString(R.string.convenient_lot_with_many_spaces_at_the_center_of_campus),
                getString(
                    R.string._855_curtis_street
                )),
            LocationData(getString(R.string.juniper_lot), R.drawable.juniper_lot, "$6.50",
                getString(
                    R.string.less_traffic_quieter
                ), getString(R.string._660_curtis_street)),
            LocationData(getString(R.string.maple_lot), R.drawable.maple_lot, "$8.00",
                getString(R.string.a_smaller_lot_near_the_cherry_creek_academic_building),
                getString(
                    R.string._921_street_francis_way
                )),
            LocationData("Nutmeg Lot", R.drawable.nutmeg_lot, "$8.00",
                getString(R.string.a_very_limited_lot_mostly_used_by_faculty),
                getString(R.string._1155_street_francis_way)),
            LocationData(getString(R.string.oak_lot), R.drawable.oak_lot, "$8.00", "A very limited lot mostly used by faculty", "1025 Street Francis Way"),
            LocationData(getString(R.string.spruce_lot), R.drawable.spruce_lot, "$8.00",
                getString(R.string.great_lot_at_the_center_of_the_msu_campus),
                getString(R.string._800_walnut_street)),
            LocationData(getString(R.string.walnut_lot), R.drawable.walnut_lot, "$5.00",
                getString(R.string.a_great_lot_with_great_savings),
                getString(R.string._1333_4th_street))
        )



        // Set up adapters for garages and parking lots
        val garageAdapter = LocationExpandableAdapter(this, garageList)
        val lotAdapter = LocationExpandableAdapter(this, lotList)

        // Assign each adapter to its respective ExpandableListView
        expandableListGarages.setAdapter(garageAdapter)
        expandableListParkingLots.setAdapter(lotAdapter)
    }
}
