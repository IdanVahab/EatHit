package com.example.eathit

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

/**
 * ConfirmationActivity:
 * This activity displays a confirmation animation and reservation details after a successful booking.
 * It also includes a rating dialog for user feedback and a back button to navigate to the previous screen.
 */
class ConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        // UI components
        val confirmationAnimation: LottieAnimationView = findViewById(R.id.confirmationAnimation)
        val reservationDetails: TextView = findViewById(R.id.reservationDetails)
        val backButton: Button = findViewById(R.id.backButton)

        // Retrieve reservation details from the Intent
        val fullName = intent.getStringExtra("FULL_NAME") ?: "N/A"
        val phone = intent.getStringExtra("PHONE") ?: "N/A"
        val email = intent.getStringExtra("EMAIL") ?: "N/A"
        val date = intent.getStringExtra("DATE") ?: "N/A"
        val time = intent.getStringExtra("TIME") ?: "N/A"
        val numberOfPeople = intent.getStringExtra("NUMBER_OF_PEOPLE") ?: "N/A"
        val seatingPreference = intent.getStringExtra("SEATING_PREFERENCE") ?: "N/A"

        // Format the reservation details
        val reservationText = getString(
            R.string.reservation_details_template,
            fullName,
            phone,
            email,
            date,
            time,
            numberOfPeople,
            seatingPreference
        )

        reservationDetails.text = reservationText
        reservationDetails.alpha = 0f // Initially hide the details
        // Set up the confirmation animation
        confirmationAnimation.setAnimation(R.raw.confirmation)
        confirmationAnimation.repeatCount = 0
        confirmationAnimation.playAnimation()

        // Listener to show reservation details after the animation ends
        confirmationAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                reservationDetails.animate().alpha(1f).duration = 500
                showRatingDialog()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })

        // Back button with a scaling animation and navigation to the previous activity
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
                            startActivity(Intent(this,NextActivity::class.java))
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        }.start()
                }.start()

        }
    }

    /**
     * Shows a dialog to allow the user to rate their experience.
     * Includes a RatingBar and feedback message based on the user's input.
     */
    private fun showRatingDialog() {
        // Inflate the custom rating dialog layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_rating, null)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)

        // Build and display the rating dialog
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.rate_your_experience))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.submit)) { _, _ ->
                val rating = ratingBar.rating
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
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

}
