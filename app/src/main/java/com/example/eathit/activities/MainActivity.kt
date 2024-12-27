package com.example.eathit.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.example.eathit.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Splash screen activity that displays the app logo and an animation.
 * Includes fade-in and fade-out effects before transitioning to the next activity.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        val logo = findViewById<ImageView>(R.id.logo)
        val homeAnimation = findViewById<LottieAnimationView>(R.id.homeAnimation)

        // Start splash screen animations
        startSplashScreen(logo, homeAnimation)
    }

    /**
     * Handles the splash screen animations and transitions to the next activity.
     */
    private fun startSplashScreen(logo: ImageView, homeAnimation: LottieAnimationView) {
        lifecycleScope.launch {
            // Set initial transparency
            logo.alpha = 0f
            homeAnimation.alpha = 0f

            // Fade-in animations
            fadeInViews(logo, homeAnimation)

            // Delay for display duration
            delay(4000)

            // Fade-out and move animations
            fadeOutAndMoveViews(logo, homeAnimation)

            // Transition to the next activity
            navigateToNextActivity()
        }
    }

    /**
     * Animates the views to fade in.
     */
    private suspend fun fadeInViews(logo: ImageView, homeAnimation: LottieAnimationView) {
        logo.animate().alpha(1f).setDuration(1500).start()
        homeAnimation.animate().alpha(1f).setDuration(1500).start()
        delay(1500) // Wait for fade-in to complete
    }

    /**
     * Animates the views to fade out and move off screen.
     */
    private suspend fun fadeOutAndMoveViews(logo: ImageView, homeAnimation: LottieAnimationView) {
        logo.animate().translationY(-1600f).alpha(0f).setDuration(800).start()
        homeAnimation.animate().translationY(1400f).alpha(0f).setDuration(800).start()
        delay(800) // Wait for fade-out to complete
    }

    /**
     * Navigates to the next activity with transition animations.
     */
    private fun navigateToNextActivity() {
        startActivity(Intent(this, NextActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish() // Remove MainActivity from the back stack
    }
}
