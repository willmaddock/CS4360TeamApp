package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R

class ParkingBudgetSimulatorActivity : AppCompatActivity() {

    private lateinit var budgetTypeRadioGroup: RadioGroup
    private lateinit var nextButton: Button
    private var selectedBudgetType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_budget_simulator) // Ensure this XML layout exists

        budgetTypeRadioGroup = findViewById(R.id.budget_type_radio_group)
        nextButton = findViewById(R.id.next_button)

        budgetTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedBudgetType = when (checkedId) {
                R.id.radio_daily_budget -> "Daily"
                R.id.radio_semester_budget -> "Semester"
                else -> null
            }
        }

        nextButton.setOnClickListener {
            if (selectedBudgetType != null) {
                val intent = Intent(this, SelectParkingLotActivity::class.java)
                intent.putExtra("selected_budget_type", selectedBudgetType)
                startActivity(intent)
            } else {
                // Optionally, show a message that a budget type must be selected
            }
        }
    }
}