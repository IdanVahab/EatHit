package com.example.eathit

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        val menuButton: Button = findViewById(R.id.menuButton)
        menuButton.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        val reservationButton: Button = findViewById(R.id.reservationButton)
        reservationButton.setOnClickListener {
            startActivity(Intent(this, ReservationActivity::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        val sloganText = findViewById<TextView>(R.id.sloganText)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        sloganText.startAnimation(fadeInAnimation)
    }

}
