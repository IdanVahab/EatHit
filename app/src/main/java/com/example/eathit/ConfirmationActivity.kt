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
        val fullName = intent.getStringExtra("FULL_NAME")
        val phone = intent.getStringExtra("PHONE")
        val email = intent.getStringExtra("EMAIL")
        val date = intent.getStringExtra("DATE")
        val time = intent.getStringExtra("TIME")
        val numberOfPeople = intent.getStringExtra("NUMBER_OF_PEOPLE")
        val seatingPreference = intent.getStringExtra("SEATING_PREFERENCE")

        val reservationText = """
            Full Name: $fullName
            Phone: $phone
            Email: $email
            Date: $date
            Time: $time
            Number of Guests: $numberOfPeople
            Seating Preference: $seatingPreference
        """.trimIndent()

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
            .setTitle("Rate Your Experience")
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                val rating = ratingBar.rating
                val message = if (rating > 0) {
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
            .setNegativeButton("Cancel", null)
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
