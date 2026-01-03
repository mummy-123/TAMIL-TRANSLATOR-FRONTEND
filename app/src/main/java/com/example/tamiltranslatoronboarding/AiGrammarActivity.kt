package com.example.tamiltranslatoronboarding

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AiGrammarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_grammar)

        val etInput = findViewById<EditText>(R.id.etInput)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val btnApply = findViewById<Button>(R.id.btnApply)

        findViewById<Button>(R.id.btnCheck).setOnClickListener {
            val text = etInput.text.toString()

            if (text.isEmpty()) {
                Toast.makeText(this, "Enter text first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (text.contains("your", true)) {
                tvResult.text =
                    "Suggestion:\nConsider using \"you're\" instead of \"your\""
                btnApply.visibility = Button.VISIBLE
            } else {
                tvResult.text =
                    "Correct Usage:\nYour grammar is perfect!"
                btnApply.visibility = Button.GONE
            }
        }

        btnApply.setOnClickListener {
            etInput.setText(
                etInput.text.toString().replace("your", "you're", true)
            )
            Toast.makeText(this, "Applied", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnContext).setOnClickListener {
            tvResult.text = "Context:\nSentence meaning explained."
        }

        findViewById<Button>(R.id.btnExamples).setOnClickListener {
            tvResult.text = "Examples:\nYou're learning.\nYou're improving."
        }

        findViewById<Button>(R.id.btnPronounce).setOnClickListener {
            Toast.makeText(this, "Pronunciation clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
