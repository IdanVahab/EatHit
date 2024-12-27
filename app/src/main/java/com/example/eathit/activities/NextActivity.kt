package com.example.eathit.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.eathit.R

/**
 * Navigation screen providing options to access the menu or reservation pages.
 * Includes animations for button interactions and a fade-in effect for the slogan text.
 */
class NextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        // Initialize UI elements
        val menuButton: Button = findViewById(R.id.menuButton)
        val reservationButton: Button = findViewById(R.id.reservationButton)
        val sloganText: TextView = findViewById(R.id.sloganText)

        // Set up button click listeners
        setupButtonAnimation(menuButton, MenuActivity::class.java)
        setupButtonAnimation(reservationButton, ReservationActivity::class.java)

        // Animate slogan text with fade-in
        animateSloganText(sloganText)
    }

    /**
     * Sets up a button with scaling animation and navigation to the specified activity.
     */
    private fun setupButtonAnimation(button: Button, targetActivity: Class<*>) {
        button.setOnClickListener {
            button.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(150)
                .withEndAction {
                    button.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .withEndAction {
                            startActivity(Intent(this, targetActivity))
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        }.start()
                }.start()
        }
    }

    /**
     * Animates the slogan text with a fade-in effect.
     */
    private fun animateSloganText(textView: TextView) {
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        textView.startAnimation(fadeInAnimation)
    }
}
