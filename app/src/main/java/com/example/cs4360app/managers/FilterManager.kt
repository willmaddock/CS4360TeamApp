package com.example.cs4360app.managers

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import com.example.cs4360app.R
import com.google.android.gms.maps.GoogleMap

object FilterManager {

    fun showFilterDialog(context: Context, mMap: GoogleMap) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_filter_options, null)
        val priceCheckBox: CheckBox = dialogView.findViewById(R.id.checkbox_price)
        val availabilityCheckBox: CheckBox = dialogView.findViewById(R.id.checkbox_availability)
        val proximityCheckBox: CheckBox = dialogView.findViewById(R.id.checkbox_proximity)
        val ratingCheckBox: CheckBox = dialogView.findViewById(R.id.checkbox_rating)
        val addressCheckBox: CheckBox = dialogView.findViewById(R.id.checkbox_address)
        val applyFiltersButton: Button = dialogView.findViewById(R.id.button_apply_filters)

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