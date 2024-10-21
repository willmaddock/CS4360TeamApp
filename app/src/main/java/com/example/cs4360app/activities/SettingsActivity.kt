@file:Suppress("DEPRECATION")

package com.example.cs4360app.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
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
        val languages = arrayOf(getString(R.string.english), getString(R.string.spanish), getString(
            R.string.chinese
        ))
        val languageCodes = arrayOf("en", "es", "zh") // Corresponding language codes

        // Inflate the custom layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_language_selection, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        val languageListView: ListView = dialogView.findViewById(R.id.language_list)
        val cancelButton: Button = dialogView.findViewById(R.id.button_cancel)

        // Set up the ListView with languages
        languageListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, languages)

        // Handle language selection
        languageListView.setOnItemClickListener { _, _, position, _ ->
            // Show confirmation dialog
            showConfirmationDialog(languageCodes[position])
        }

        // Show the dialog
        val dialog = builder.create()

        // Handle cancel button
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    // Show confirmation dialog for language change
    private fun showConfirmationDialog(languageCode: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.confirm_language_change))
            .setMessage(getString(R.string.are_you_sure_you_want_to_change_the_language))
            .setPositiveButton(getString(R.string.yes)) { _, _ -> setLocale(languageCode) }
            .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
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