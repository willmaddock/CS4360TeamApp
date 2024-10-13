package com.example.cs4360app.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R
import java.text.SimpleDateFormat
import java.util.*

class ParkingBudgetSimulatorActivity : AppCompatActivity() {

    private lateinit var budgetTypeRadioGroup: RadioGroup
    private lateinit var nextButton: Button
    private lateinit var startDateButton: Button
    private lateinit var endDateButton: Button
    private lateinit var semesterDatesLabel: TextView
    private lateinit var startDateLabel: TextView
    private lateinit var endDateLabel: TextView
    private lateinit var parkingMeterButton: Button

    private var selectedBudgetType: String? = null
    private var startDate: String? = null
    private var endDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_budget_simulator)

        budgetTypeRadioGroup = findViewById(R.id.budget_type_radio_group)
        nextButton = findViewById(R.id.next_button)
        startDateButton = findViewById(R.id.start_date_button)
        endDateButton = findViewById(R.id.end_date_button)
        semesterDatesLabel = findViewById(R.id.semester_dates_label)
        startDateLabel = findViewById(R.id.start_date_label)
        endDateLabel = findViewById(R.id.end_date_label)
        parkingMeterButton = findViewById(R.id.parking_meter_button)

        val calendar = Calendar.getInstance()

        // Handle budget type selection
        budgetTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_daily_budget -> {
                    selectedBudgetType = "Daily"
                    hideSemesterDateSelection()
                }
                R.id.radio_semester_budget -> {
                    selectedBudgetType = "Semester"
                    showSemesterDateSelection()
                }
            }
        }

        // Start Date Picker
        startDateButton.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val selectedDate = formatDate(year, month, dayOfMonth)
                startDate = selectedDate
                startDateButton.text = selectedDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // End Date Picker
        endDateButton.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val selectedDate = formatDate(year, month, dayOfMonth)
                endDate = selectedDate
                endDateButton.text = selectedDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Handle Next button (Parking Lot Selection)
        nextButton.setOnClickListener {
            if (selectedBudgetType != null) {
                if (selectedBudgetType == "Semester" && (startDate == null || endDate == null)) {
                    Toast.makeText(this, "Please select both start and end dates for the semester.", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, SelectParkingLotActivity::class.java)
                    intent.putExtra("selected_budget_type", selectedBudgetType)
                    intent.putExtra("start_date", startDate)
                    intent.putExtra("end_date", endDate)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Please select a budget type.", Toast.LENGTH_SHORT).show()
            }
        }

        // Parking Meter Button (Only access to PayParkingMeterActivity)
        parkingMeterButton.setOnClickListener {
            val intent = Intent(this, PayParkingMeterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(calendar.time)
    }

    private fun showSemesterDateSelection() {
        semesterDatesLabel.visibility = TextView.VISIBLE
        startDateLabel.visibility = TextView.VISIBLE
        startDateButton.visibility = Button.VISIBLE
        endDateLabel.visibility = TextView.VISIBLE
        endDateButton.visibility = Button.VISIBLE
    }

    private fun hideSemesterDateSelection() {
        semesterDatesLabel.visibility = TextView.GONE
        startDateLabel.visibility = TextView.GONE
        startDateButton.visibility = TextView.GONE
        endDateLabel.visibility = TextView.GONE
        endDateButton.visibility = TextView.GONE
    }
}