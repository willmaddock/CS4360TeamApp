package com.example.cs4360app.managers

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.cs4360app.R
import com.google.android.gms.maps.GoogleMap

object FilterManager {

    private const val PREFS_NAME = "filter_prefs"
    private const val KEY_SHOW_PRICE = "show_price"
    private const val KEY_SHOW_AVAILABILITY = "show_availability"
    private const val KEY_SHOW_PROXIMITY = "show_proximity"
    private const val KEY_SHOW_RATING = "show_rating"
    private const val KEY_SHOW_ADDRESS = "show_address"

    fun showFilterDialog(context: Context, mMap: GoogleMap) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_filter_options, null)
        val priceCheckBox: CheckBox = dialogView.findViewById(R.id.checkbox_price)
        val availabilityCheckBox: CheckBox = dialogView.findViewById(R.id.checkbox_availability)
        val proximityCheckBox: CheckBox = dialogView.findViewById(R.id.checkbox_proximity)
        val ratingCheckBox: CheckBox = dialogView.findViewById(R.id.checkbox_rating)
        val addressCheckBox: CheckBox = dialogView.findViewById(R.id.checkbox_address)
        val applyFiltersButton: Button = dialogView.findViewById(R.id.button_apply_filters)

        // Load saved preferences
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        priceCheckBox.isChecked = sharedPreferences.getBoolean(KEY_SHOW_PRICE, false)
        availabilityCheckBox.isChecked = sharedPreferences.getBoolean(KEY_SHOW_AVAILABILITY, false)
        proximityCheckBox.isChecked = sharedPreferences.getBoolean(KEY_SHOW_PROXIMITY, false)
        ratingCheckBox.isChecked = sharedPreferences.getBoolean(KEY_SHOW_RATING, false)
        addressCheckBox.isChecked = sharedPreferences.getBoolean(KEY_SHOW_ADDRESS, false)

        // Show a pop-up explaining AI-based and historical filtering
        Toast.makeText(context, "Filter options are based on historical data and AI simulations", Toast.LENGTH_LONG).show()

        // Create AlertDialog
        val builder = AlertDialog.Builder(context)
            .setTitle("Filter Options")
            .setView(dialogView)

        val alertDialog = builder.create()

        applyFiltersButton.setOnClickListener {
            // Get filter values
            val showPrice = priceCheckBox.isChecked
            val showAvailability = availabilityCheckBox.isChecked
            val showProximity = proximityCheckBox.isChecked
            val showRating = ratingCheckBox.isChecked
            val showAddress = addressCheckBox.isChecked

            // Save filter preferences
            with(sharedPreferences.edit()) {
                putBoolean(KEY_SHOW_PRICE, showPrice)
                putBoolean(KEY_SHOW_AVAILABILITY, showAvailability)
                putBoolean(KEY_SHOW_PROXIMITY, showProximity)
                putBoolean(KEY_SHOW_RATING, showRating)
                putBoolean(KEY_SHOW_ADDRESS, showAddress)
                apply()
            }

            // Load parking lots with selected filters
            ParkingLotManager.loadParkingLots(
                mMap,
                showPrice = showPrice,
                showAvailability = showAvailability,
                showProximity = showProximity,
                showRating = showRating,
                showAddress = showAddress
            )

            alertDialog.dismiss() // Dismiss the dialog after applying filters
        }

        alertDialog.show() // Show the dialog
    }
}