package com.example.tamiltranslatoronboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.tamiltranslatoronboarding.session.SessionManager

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = SessionManager(this)
        if (session.isLoggedIn()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_welcome)

        val btn = findViewById<Button>(R.id.btnGetStarted)
        btn.setOnClickListener {
            startActivity(Intent(this, OnboardingActivity::class.java))
        }
    }
}
