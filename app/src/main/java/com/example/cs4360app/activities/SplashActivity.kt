package com.example.cs4360app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val splashDisplayLength: Long = 3000 // 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Automatically transition to MapsActivity after the splash duration
        Handler().postDelayed({
            // Directly go to MapsActivity
            startActivity(Intent(this, MapsActivity::class.java))
            finish() // Finish SplashActivity
        }, splashDisplayLength)
    }
}