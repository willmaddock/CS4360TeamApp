package com.example.cs4360app.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs4360app.R
import com.example.cs4360app.databinding.ActivityMainBinding
import com.example.cs4360app.managers.MainMenuManager
import com.google.firebase.FirebaseApp
import java.util.Locale

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainMenuManager: MainMenuManager

    companion object {
        private var instance: MainActivity? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale(loadLanguage())
        FirebaseApp.initializeApp(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        instance = this // Set instance when the activity is created

        // Initialize MainMenuManager
        mainMenuManager = MainMenuManager(this, binding)
        mainMenuManager.initializeMenu()

        // Setup recycler view for reviews
        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(this)
        fetchReviews()
        setupLanguageSelector()  // Set up the spinner
    }

    private fun fetchReviews() {
        // Your fetch reviews code here...
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun saveLanguage(languageCode: String) {
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString("language", languageCode).apply()
    }

    private fun loadLanguage(): String {
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getString("language", "en") ?: "en"
    }

    override fun onResume() {
        super.onResume()
        mainMenuManager.checkAndShowActiveTimer() // Check and show timer when activity resumes
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null // Clear instance when activity is destroyed
    }

    private fun setupLanguageSelector() {
        val spinner: Spinner = binding.languageSpinner

        // Create an ArrayAdapter for the spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.languages_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Set the listener for spinner item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val selectedLanguage = if (position == 0) "en" else "es"
                if (selectedLanguage != loadLanguage()) {
                    saveLanguage(selectedLanguage)
                    recreate()  // Restart activity to apply the new language
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }
}