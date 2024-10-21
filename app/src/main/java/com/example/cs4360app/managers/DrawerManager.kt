package com.example.cs4360app.managers

import android.content.Context
import android.content.Intent
import com.example.cs4360app.R
import com.example.cs4360app.activities.*
import com.google.android.material.navigation.NavigationView

object DrawerManager {

    fun setupDrawer(context: Context, navigationView: NavigationView) {
        // Set up navigation item selection listener
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_parking_budget_simulator -> {
                    context.startActivity(Intent(context, ParkingBudgetSimulatorActivity::class.java))
                    true
                }
                R.id.nav_chat -> {
                    context.startActivity(Intent(context, ChatActivity::class.java))
                    true
                }
                R.id.nav_notifications -> {
                    context.startActivity(Intent(context, NotificationsActivity::class.java))
                    true
                }
                R.id.nav_timer -> {
                    context.startActivity(Intent(context, TimerActivity::class.java))
                    true
                }
                R.id.nav_submit_review -> {
                    context.startActivity(Intent(context, SubmitReviewActivity::class.java))
                    true
                }
                R.id.nav_submit_petition -> {
                    context.startActivity(Intent(context, PetitionActivity::class.java))
                    true
                }
                R.id.nav_take_survey -> {
                    context.startActivity(Intent(context, SurveyActivity::class.java))
                    true
                }

                R.id.nav_settings -> {
                    context.startActivity(Intent(context, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        updateMenuForUser(context, navigationView)
    }

    private fun updateMenuForUser(context: Context, navigationView: NavigationView) {
        val menu = navigationView.menu
        val isTimerActive = isTimerActive(context)

        // Menu options visibility based on conditions
        menu.findItem(R.id.nav_parking_budget_simulator).isVisible = true // Always visible
        menu.findItem(R.id.nav_timer).isVisible = isTimerActive // Only show active timer
        menu.findItem(R.id.nav_chat).isVisible = true
        menu.findItem(R.id.nav_submit_petition).isVisible = true
        menu.findItem(R.id.nav_submit_review).isVisible = true
        menu.findItem(R.id.nav_notifications).isVisible = true
        menu.findItem(R.id.nav_take_survey).isVisible = true
        menu.findItem(R.id.nav_settings).isVisible = true
    }

    // Check if the timer is active or expired
    private fun isTimerActive(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("payment_prefs", Context.MODE_PRIVATE)
        val endTime = sharedPreferences.getLong("end_time", 0)
        // Timer is active if the end time is in the future
        return endTime > System.currentTimeMillis()
    }
}