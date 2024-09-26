package com.example.cs4360app.activities

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class TimerService : Service() {

    companion object {
        const val COUNTDOWN_BR = "com.example.cs4360app.activities"
        const val COUNTDOWN_TIME = "countdown_time"
    }

    private lateinit var timer: CountDownTimer

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val parkingTime = intent?.getIntExtra("parkingTime", 0) ?: 0
        Log.d("TimerService", "Service started with parking time: $parkingTime")
        timer = object : CountDownTimer(parkingTime.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val broadcastIntent = Intent(COUNTDOWN_BR)
                broadcastIntent.putExtra(COUNTDOWN_TIME, millisUntilFinished)
                sendBroadcast(broadcastIntent)
                Log.d("TimerService", "Broadcast sent with remaining time: $millisUntilFinished")
            }

            override fun onFinish() {
                val broadcastIntent = Intent(COUNTDOWN_BR)
                broadcastIntent.putExtra("timer_finished", true)
                sendBroadcast(broadcastIntent)
                Log.d("TimerService", "Broadcast sent: timer finished")
                stopSelf()
            }
        }.start()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }


}