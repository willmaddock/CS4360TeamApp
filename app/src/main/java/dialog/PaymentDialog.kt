import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.cs4360app.R

class PaymentDialog(context: Context, private val onPaymentConfirmed: (String) -> Unit) : Dialog(context) {
    private lateinit var cardInput: EditText
    private lateinit var confirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_payment)

        cardInput = findViewById(R.id.card_input)
        confirmButton = findViewById(R.id.confirm_button)

        confirmButton.setOnClickListener {
            val cardNumber = cardInput.text.toString().trim()
            if (cardNumber.isNotEmpty()) {
                onPaymentConfirmed(cardNumber) // Return card number to PayParkingMeterActivity
                dismiss()
            }
        }
    }
}