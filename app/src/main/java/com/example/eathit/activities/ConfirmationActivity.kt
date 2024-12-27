package com.example.eathit.activities

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.eathit.R

/**
 * Displays a confirmation animation and reservation details after a successful booking.
 * Includes a rating dialog for user feedback and navigation back to the main menu.
 */
class ConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        // Initialize views
        val confirmationAnimation: LottieAnimationView = findViewById(R.id.confirmationAnimation)
        val reservationDetails: TextView = findViewById(R.id.reservationDetails)
        val backButton: Button = findViewById(R.id.backButton)

        // Display reservation details
        setupReservationDetails(reservationDetails)

        // Set up and play confirmation animation
        setupConfirmationAnimation(confirmationAnimation, reservationDetails)

        // Back button setup
        setupBackButton(backButton)
    }

    /**
     * Retrieves and formats reservation details from the Intent, then displays them.
     */
    private fun setupReservationDetails(reservationDetails: TextView) {
        val fullName = intent.getStringExtra("FULL_NAME") ?: "N/A"
        val phone = intent.getStringExtra("PHONE") ?: "N/A"
        val email = intent.getStringExtra("EMAIL") ?: "N/A"
        val date = intent.getStringExtra("DATE") ?: "N/A"
        val time = intent.getStringExtra("TIME") ?: "N/A"
        val numberOfPeople = intent.getStringExtra("NUMBER_OF_PEOPLE") ?: "N/A"
        val seatingPreference = intent.getStringExtra("SEATING_PREFERENCE") ?: "N/A"

        val reservationText = getString(
            R.string.reservation_details_template,
            fullName, phone, email, date, time, numberOfPeople, seatingPreference
        )

        reservationDetails.text = reservationText
        reservationDetails.alpha = 0f // Hide initially
    }

    /**
     * Configures and plays the confirmation animation, then shows the reservation details.
     */
    private fun setupConfirmationAnimation(
        confirmationAnimation: LottieAnimationView,
        reservationDetails: TextView
    ) {
        confirmationAnimation.setAnimation(R.raw.confirmation)
        confirmationAnimation.repeatCount = 0
        confirmationAnimation.playAnimation()

        confirmationAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                reservationDetails.animate().alpha(1f).duration = 500
                showRatingDialog()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    /**
     * Configures the back button with animation and navigation to the next activity.
     */
    private fun setupBackButton(backButton: Button) {
        backButton.setOnClickListener {
            backButton.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .withEndAction {
                    backButton.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .withEndAction {
                            navigateToNextActivity()
                        }.start()
                }.start()
        }
    }

    /**
     * Navigates to the NextActivity with transition animation.
     */
    private fun navigateToNextActivity() {
        startActivity(Intent(this, NextActivity::class.java))
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    /**
     * Shows a dialog for the user to rate their experience.
     */
    private fun showRatingDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_rating, null)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.rate_your_experience))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.submit)) { _, _ ->
                val rating = ratingBar.rating
                showFeedbackDialog(rating)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    /**
     * Displays a thank-you dialog with feedback based on the user's rating.
     */
    private fun showFeedbackDialog(rating: Float) {
        val message = if (rating > 0) {
            getString(R.string.feedback_message_with_rating, rating)
        } else {
            getString(R.string.feedback_message_without_rating)
        }

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.thank_you_title))
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
