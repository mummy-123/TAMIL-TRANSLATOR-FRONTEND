package com.example.tamiltranslatoronboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)

        val score = intent.getIntExtra("score",0)
        val total = intent.getIntExtra("total",1)
        val percent = (score * 100) / total

        findViewById<TextView>(R.id.txtScore).text = "$percent%"

        findViewById<Button>(R.id.btnHome).setOnClickListener { finish() }
        findViewById<Button>(R.id.btnRetry).setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
            finish()
        }
    }
}