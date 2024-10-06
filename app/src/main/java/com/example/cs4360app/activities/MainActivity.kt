package com.example.cs4360app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs4360app.databinding.ActivityMainBinding
import com.example.cs4360app.managers.MainMenuManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mainMenuManager: MainMenuManager

    companion object {
        private var instance: MainActivity? = null

        fun getInstance(): MainActivity? {
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        instance = this // Set instance when the activity is created
        auth = FirebaseAuth.getInstance()

        // Initialize MainMenuManager
        mainMenuManager = MainMenuManager(this, binding, auth)
        mainMenuManager.initializeMenu()

        // Setup recycler view for reviews
        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(this)
        fetchReviews()
    }

    private fun fetchReviews() {
        // Your fetch reviews code...
    }

    override fun onResume() {
        super.onResume()
        mainMenuManager.checkAndShowActiveTimer() // Check and show timer when activity resumes
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null // Clear instance when activity is destroyed
    }
}