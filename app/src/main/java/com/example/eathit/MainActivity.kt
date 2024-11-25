package com.example.eathit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logo = findViewById<ImageView>(R.id.logo)
        val homeAnimation = findViewById<LottieAnimationView>(R.id.homeAnimation)


        lifecycleScope.launch {
            // התחלה עם fade-in
            logo.alpha = 0f
            homeAnimation.alpha = 0f
            logo.animate().alpha(1f).setDuration(1500).start()
            homeAnimation.animate().alpha(1f).setDuration(1500).start()

            // אנימציית יציאה
            delay(4000)
            logo.animate().translationY(-1600f).alpha(0f).setDuration(800).start()
            homeAnimation.animate().translationY(1400f).alpha(0f).setDuration(800).start()

            // מעבר למסך הבא
            delay(1000)
            startActivity(Intent(this@MainActivity, NextActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
    }
}
