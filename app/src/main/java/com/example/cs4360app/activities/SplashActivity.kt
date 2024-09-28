package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH: Long = 3000 // 3 seconds
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Automatically transition based on login status after the splash duration
        Handler().postDelayed({
            if (auth.currentUser != null) {
                // User is signed in, go to MapsActivity
                startActivity(Intent(this, MapsActivity::class.java))
            } else {
                // User is not signed in, go to LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish() // Finish SplashActivity
        }, SPLASH_DISPLAY_LENGTH)
    }
}