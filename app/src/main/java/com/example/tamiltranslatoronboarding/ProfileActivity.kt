package com.example.tamiltranslatoronboarding

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tamiltranslatoronboarding.session.SessionManager

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val btnLogout = findViewById<TextView>(R.id.txtLogout)

        btnLogout.setOnClickListener {
            SessionManager(this).logout()
            startActivity(Intent(this, WelcomeActivity::class.java))
            finishAffinity()
        }
    }
}
