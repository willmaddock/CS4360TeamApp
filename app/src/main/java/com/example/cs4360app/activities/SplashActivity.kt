package com.example.cs4360app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4360app.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val videoView: VideoView = findViewById(R.id.intro_video_view)

        // Set the path to your video file in the raw resource folder
        val videoPath = "android.resource://" + packageName + "/" + R.raw.ampintro
        val uri: Uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)

        // Ensure the video fills the screen properly
        videoView.setOnPreparedListener { mediaPlayer ->
            val videoWidth = mediaPlayer.videoWidth
            val videoHeight = mediaPlayer.videoHeight
            val screenWidth = resources.displayMetrics.widthPixels
            val screenHeight = resources.displayMetrics.heightPixels

            val layoutParams = videoView.layoutParams

            // Calculate scaling ratios and adjust layout to fit the screen
            val videoProportion = videoHeight.toFloat() / videoWidth.toFloat()
            val screenProportion = screenHeight.toFloat() / screenWidth.toFloat()

            if (videoProportion > screenProportion) {
                // Video is taller than the screen, scale based on height
                layoutParams.height = screenHeight
                layoutParams.width = (screenHeight.toFloat() / videoProportion).toInt()
            } else {
                // Video is wider than the screen, scale based on width
                layoutParams.width = screenWidth
                layoutParams.height = (screenWidth.toFloat() * videoProportion).toInt()
            }

            videoView.layoutParams = layoutParams
            videoView.start()
        }

        // Transition to MapsActivity when the video finishes
        videoView.setOnCompletionListener {
            startActivity(Intent(this, MapsActivity::class.java))
            finish() // Close the SplashActivity
        }
    }
}
