package com.example.eathit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * MainActivity:
 * This activity serves as the splash screen of the app.
 * It displays a logo and an animation with fade-in and fade-out effects.
 * After a delay, it transitions to the NextActivity with a fade effect.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Reference to the logo ImageView from the layout
        val logo = findViewById<ImageView>(R.id.logo)

        // Reference to the Lottie animation view for the home animation
        val homeAnimation = findViewById<LottieAnimationView>(R.id.homeAnimation)

        // Launching a coroutine to handle animations and transitions
        lifecycleScope.launch {
            // Initial state: both logo and animation are fully transparent
            logo.alpha = 0f
            homeAnimation.alpha = 0f

            // Animate the logo and animation to fade in over 1.5 seconds
            logo.animate().alpha(1f).setDuration(1500).start()
            homeAnimation.animate().alpha(1f).setDuration(1500).start()

            // Wait for 4 seconds before starting the fade-out animations
            delay(4000)

            // Animate the logo to move upwards and fade out
            logo.animate().translationY(-1600f).alpha(0f).setDuration(800).start()

            // Animate the home animation to move downwards and fade out
            homeAnimation.animate().translationY(1400f).alpha(0f).setDuration(800).start()

            // Wait for the animations to finish before transitioning to the next activity
            delay(1000)

            // Start the next activity and apply a fade-in, fade-out transition effect
            startActivity(Intent(this@MainActivity, NextActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

            // Finish the current activity to remove it from the back stack
            finish()
        }
    }
}
