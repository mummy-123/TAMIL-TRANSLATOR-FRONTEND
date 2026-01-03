package com.example.tamiltranslatoronboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ReadyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ready)

        val btnCreateAccount = findViewById<Button>(R.id.btnCreateAccount)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)

        btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        btnSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}