package com.example.tamiltranslatoronboarding

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tamiltranslatoronboarding.R

class QuizActivity : AppCompatActivity() {

    private val questions = listOf(
        Triple("What is the Tamil word for 'Hello'?", "வணக்கம்",
            listOf("நன்றி","வணக்கம்","காலை","அமைதி")),
        Triple("What is Tamil for 'Friend'?", "நண்பன்",
            listOf("கனவு","நண்பன்","அன்பு","சந்தோஷம்")),
        Triple("Tamil word for 'Love'?", "அன்பு",
            listOf("அமைதி","அன்பு","வெற்றி","பயம்")),
        Triple("Tamil for 'Peace'?", "அமைதி",
            listOf("அமைதி","நம்பிக்கை","அன்பு","துணிவு")),
        Triple("Tamil for 'Hope'?", "நம்பிக்கை",
            listOf("உண்மை","நம்பிக்கை","வெற்றி","கனவு")),
        Triple("Tamil for 'Smile'?", "புன்னகை",
            listOf("பயம்","புன்னகை","துணிவு","நன்றி")),
        Triple("Tamil for 'Dream'?", "கனவு",
            listOf("கனவு","அன்பு","உண்மை","வெற்றி")),
        Triple("Tamil for 'Truth'?", "உண்மை",
            listOf("துணிவு","அமைதி","உண்மை","கனவு")),
        Triple("Tamil for 'Brave'?", "துணிவு",
            listOf("நன்றி","பயம்","துணிவு","அன்பு")),
        Triple("Tamil for 'Thank You'?", "நன்றி",
            listOf("நன்றி","கனவு","அமைதி","அன்பு"))
    )

    private var index = 0
    private val answers = IntArray(10) { -1 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        loadQuestion()

        findViewById<Button>(R.id.btnNext).setOnClickListener { saveAndNext() }
        findViewById<Button>(R.id.btnPrev).setOnClickListener {
            if (index > 0) {
                index--
                loadQuestion()
            }
        }
    }

    private fun saveAndNext() {
        val group = findViewById<RadioGroup>(R.id.radioGroup)
        val checked = group.checkedRadioButtonId
        if (checked != -1) {
            answers[index] = group.indexOfChild(findViewById(checked))
        }

        if (index < questions.size - 1) {
            index++
            loadQuestion()
        } else {
            calculateResult()
        }
    }

    private fun loadQuestion() {
        val q = questions[index]
        findViewById<TextView>(R.id.txtQNo).text = "Question ${index + 1} of ${questions.size}"
        findViewById<ProgressBar>(R.id.progressQuiz).progress = index + 1
        findViewById<TextView>(R.id.txtQuestion).text = q.first

        val group = findViewById<RadioGroup>(R.id.radioGroup)
        group.removeAllViews()

        q.third.forEachIndexed { i, opt ->
            val rb = RadioButton(this)
            rb.text = opt
            if (answers[index] == i) rb.isChecked = true
            group.addView(rb)
        }
    }

    private fun calculateResult() {
        var score = 0
        questions.forEachIndexed { i, q ->
            if (answers[i] != -1 && q.third[answers[i]] == q.second) score++
        }

        val intent = Intent(this, QuizResultActivity::class.java)
        intent.putExtra("score", score)
        intent.putExtra("total", questions.size)
        startActivity(intent)
        finish()
    }
}