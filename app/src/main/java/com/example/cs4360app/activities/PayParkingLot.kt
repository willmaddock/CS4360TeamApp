package com.example.cs4360app.activities

//import ParkingTimer
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cs4360app.MainActivity
import com.example.cs4360app.R
import com.example.cs4360app.models.ParkingLot

class PayParkingLot : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_parking_lot)

        // Retrieve the parking lot name and price from the Intent extras
        val parkingLotName = intent.getStringExtra("parkingLotName")
        val parkingLotPrice = intent.getDoubleExtra("parkingLotPrice", 0.0)

        // Use the retrieved name and price
        // For example, display them in a TextView
        val parkingLotInfoTextView: TextView = findViewById(R.id.money_reminder)
        parkingLotInfoTextView.text = "Parking Lot: $parkingLotName, Price: $$parkingLotPrice"

        val payButton: Button = findViewById(R.id.payButton)
        payButton.setOnClickListener {
            val cardName: EditText = findViewById(R.id.cardname)
            val cardNumber: EditText = findViewById(R.id.cardnumber)
            val expireDate: EditText = findViewById(R.id.expire_date)
            val cvc: EditText = findViewById(R.id.cvc)

            // Retrieve the content of each EditText
            val cardNameStr = cardName.text.toString()
            val cardNumberStr = cardNumber.text.toString()
            val expireDateStr = expireDate.text.toString()
            val cvcStr = cvc.text.toString()

            // Check if the content is valid
            if (cardNameStr.isEmpty() || cardNumberStr.isEmpty() || expireDateStr.isEmpty() || cvcStr.isEmpty()) {
                // Show an error message if any field is empty
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (cardNumberStr.length != 16) {
                // Show an error message if the card number is not 16 digits
                Toast.makeText(this, "Please enter a valid card number", Toast.LENGTH_SHORT).show()
            } else if (!expireDateStr.matches(Regex("\\d{2}/\\d{2}"))) {
                // Show an error message if the expiry date is not in the format MM/YY
                Toast.makeText(this, "Please enter a valid expiry date in the format MM/YY", Toast.LENGTH_SHORT).show()
            } else if (cvcStr.length != 3) {
                // Show an error message if the CVC is not 3 digits
                Toast.makeText(this, "Please enter a valid CVC", Toast.LENGTH_SHORT).show()
            } else {
                // If all fields are valid, proceed with the Intent
                val parkingTime = 240000 // 2 hours in milliseconds, adjust as needed
                val sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putInt("parkingTime", parkingTime)
                editor.apply()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("parkingTime", parkingTime)
                Toast.makeText(this, "Payment Completed. Thank you for paying your parking ticket", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                // After payment is successful

                Log.d("PayParkingLot", "Parking time: $parkingTime")
                val intent2 = Intent(this, ParkingTimer::class.java)
                intent2.putExtra("parkingTime", parkingTime)
                startActivity(intent2)
            }
        }
    }
}