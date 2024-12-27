package com.example.eathit.utils

import android.widget.Button

// פונקציה כללית לאנימציה על כפתור עם פעולה מותאמת
fun setupButtonWithAnimation(button: Button, action: () -> Unit) {
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
                        action()
                    }.start()
            }.start()
    }
}
