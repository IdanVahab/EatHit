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


class ConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        val confirmationAnimation: LottieAnimationView = findViewById(R.id.confirmationAnimation)
        val reservationDetails: TextView = findViewById(R.id.reservationDetails)
        val backButton: Button = findViewById(R.id.backButton)

// מקבל את הנתונים מה-Intent
        val fullName = intent.getStringExtra("FULL_NAME") ?: "N/A"
        val phone = intent.getStringExtra("PHONE") ?: "N/A"
        val email = intent.getStringExtra("EMAIL") ?: "N/A"
        val date = intent.getStringExtra("DATE") ?: "N/A"
        val time = intent.getStringExtra("TIME") ?: "N/A"
        val numberOfPeople = intent.getStringExtra("NUMBER_OF_PEOPLE") ?: "N/A"
        val seatingPreference = intent.getStringExtra("SEATING_PREFERENCE") ?: "N/A"

// שימוש בתבנית מחרוזת מתורגמת
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
        reservationDetails.alpha = 0f // מוסתר עד לסיום האנימציה

// הפעלת האנימציה
        confirmationAnimation.setAnimation(R.raw.confirmation)
        confirmationAnimation.repeatCount = 0
        confirmationAnimation.playAnimation()

        confirmationAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                reservationDetails.animate().alpha(1f).duration = 500 // הצגת פרטי ההזמנה
                showRatingDialog() // הצגת דיאלוג הדירוג
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })

        backButton.setOnClickListener {
            startActivity(Intent(this, NextActivity::class.java))
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

        }
    }

    private fun showRatingDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_rating, null)

        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.rate_your_experience)) // תרגום הכותרת
            .setView(dialogView)
            .setPositiveButton(getString(R.string.submit)) { _, _ ->
                val rating = ratingBar.rating
                val message = if (rating > 0) {
                    getString(R.string.feedback_message_with_rating, rating) // הודעה עם דירוג
                } else {
                    getString(R.string.feedback_message_without_rating) // הודעה ללא דירוג
                }

                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.thank_you_title)) // כותרת תודות
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .show()
            }
            .setNegativeButton(getString(R.string.cancel), null) // תרגום כפתור ביטול
            .show()
    }
    private fun showThankYouDialog(rating: Float?) {

        val message = if (rating != null) {
            "Thank you for your feedback! You rated us $rating stars."
        } else {
            "Thank you for visiting us!"
        }

        AlertDialog.Builder(this)
            .setTitle("Thank You")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
