package com.example.cs4360app.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        // Back button to close the settings screen
        findViewById<Button>(R.id.button_back).setOnClickListener {
            finish() // Closes the activity and returns to the previous one
        }

        // Button to open language selection dialog
        findViewById<Button>(R.id.button_change_language).setOnClickListener {
            showLanguageSelectionDialog()
        }
    }

    // Language selection dialog
    private fun showLanguageSelectionDialog() {
        val languages = arrayOf("English", "Spanish", "French")
        val languageCodes = arrayOf("en", "es", "fr") // Corresponding language codes

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose a Language")
        builder.setItems(languages) { _, which ->
            // Show confirmation dialog
            showConfirmationDialog(languageCodes[which])
        }
        builder.show()
    }

    // Show confirmation dialog for language change
    private fun showConfirmationDialog(languageCode: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Language Change")
            .setMessage("Are you sure you want to change the language?")
            .setPositiveButton("Yes") { _, _ -> setLocale(languageCode) }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    // Set the locale and refresh the UI to reflect the language change
    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        // Update the app configuration with the new locale
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Save the selected language to SharedPreferences
        with(sharedPreferences.edit()) {
            putString("app_language", languageCode)
            apply()
        }

        // Restart MapsActivity to apply the new language immediately
        restartMapsActivity()
    }

    // Method to restart MapsActivity
    private fun restartMapsActivity() {
        val intent = Intent(this, MapsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish() // Finish the SettingsActivity
    }
}