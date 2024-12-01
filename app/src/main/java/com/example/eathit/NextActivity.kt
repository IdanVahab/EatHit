package com.example.eathit

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * NextActivity:
 * This activity serves as a navigation screen, providing buttons for navigating to the menu or reservation pages.
 * It includes animations for button interactions and a fade-in animation for the slogan text.
 */
class NextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        // Button for navigating to the MenuActivity
        val menuButton: Button = findViewById(R.id.menuButton)
        menuButton.setOnClickListener {
            // Apply a scaling animation for a visual "button press" effect
            menuButton.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .withEndAction {
                    // Revert the scaling animation before navigating to MenuActivity
                    menuButton.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .withEndAction {
                            // Navigate to MenuActivity with a sliding transition
                            startActivity(Intent(this, MenuActivity::class.java))
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        }.start()
                }.start()
        }

        // Button for navigating to the ReservationActivity
        val reservationButton: Button = findViewById(R.id.reservationButton)
        reservationButton.setOnClickListener {
            // Apply a scaling animation for a visual "button press" effect
            reservationButton.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(150)
                .withEndAction {
                    // Revert the scaling animation before navigating to ReservationActivity
                    reservationButton.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .withEndAction {
                            // Navigate to ReservationActivity with a sliding transition
                            startActivity(Intent(this, ReservationActivity::class.java))
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        }.start()
                }.start()
        }

        // Slogan text with a fade-in animation
        val sloganText = findViewById<TextView>(R.id.sloganText)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        sloganText.startAnimation(fadeInAnimation)
    }
}
