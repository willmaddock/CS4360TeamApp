package com.example.cs4360app.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.MainActivity
import com.example.cs4360app.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH: Long = 3000 // 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Automatically transition to MainActivity after the splash duration
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Finish SplashActivity
        }, SPLASH_DISPLAY_LENGTH)
    }
}