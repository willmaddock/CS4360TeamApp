package com.example.cs4360app.managers

import android.content.Context
import android.content.Intent
import com.example.cs4360app.R
import com.example.cs4360app.activities.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

object DrawerManager {

    private lateinit var auth: FirebaseAuth

    fun setupDrawer(context: Context, navigationView: NavigationView) {
        auth = FirebaseAuth.getInstance()

        // Set up navigation item selection listener
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_login -> {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    true
                }
                R.id.nav_logout -> {
                    FirebaseAuth.getInstance().signOut() // Sign out the user
                    updateMenuForUser(context, navigationView) // Update menu items after logout
                    true
                }
                R.id.nav_back_to_menu -> {
                    context.startActivity(Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    })
                    true
                }
                R.id.nav_payment -> {
                    context.startActivity(Intent(context, PaymentActivity::class.java))
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
                    // Start the SurveyActivity for guest users
                    if (!isLoggedIn()) {
                        context.startActivity(Intent(context, SurveyActivity::class.java))
                    }
                    true
                }
                else -> false
            }
        }

        updateMenuForUser(context, navigationView)
    }

    private fun updateMenuForUser(context: Context, navigationView: NavigationView) {
        val isLoggedIn = auth.currentUser != null
        val menu = navigationView.menu

        // Show options based on user login status
        if (isLoggedIn) {
            menu.findItem(R.id.nav_back_to_menu).isVisible = true
            menu.findItem(R.id.nav_payment).isVisible = true
            menu.findItem(R.id.nav_timer).isVisible = isTimerActive(context) // Only show active timer
            menu.findItem(R.id.nav_chat).isVisible = true
            menu.findItem(R.id.nav_submit_petition).isVisible = true
            menu.findItem(R.id.nav_submit_review).isVisible = true
            menu.findItem(R.id.nav_notifications).isVisible = true
            menu.findItem(R.id.nav_logout).isVisible = true // Show logout option

            // Hide guest options
            menu.findItem(R.id.nav_login).isVisible = false
            menu.findItem(R.id.nav_take_survey).isVisible = false
        } else {
            // Show options for guests
            menu.findItem(R.id.nav_login).isVisible = true // Show login option
            menu.findItem(R.id.nav_payment).isVisible = true
            menu.findItem(R.id.nav_timer).isVisible = isTimerActive(context) // Only show active timer
            menu.findItem(R.id.nav_take_survey).isVisible = true // Show survey option

            // Hide logged-in user options
            menu.findItem(R.id.nav_back_to_menu).isVisible = false
            menu.findItem(R.id.nav_chat).isVisible = false
            menu.findItem(R.id.nav_submit_petition).isVisible = false
            menu.findItem(R.id.nav_submit_review).isVisible = false
            menu.findItem(R.id.nav_notifications).isVisible = false
            menu.findItem(R.id.nav_logout).isVisible = false // Hide logout option
        }
    }

    private fun isTimerActive(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("payment_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("payment_active", false)
    }

    private fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}