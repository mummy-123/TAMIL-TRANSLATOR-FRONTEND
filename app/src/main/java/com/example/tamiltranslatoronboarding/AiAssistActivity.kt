package com.example.tamiltranslatoronboarding

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AiAssistActivity : AppCompatActivity() {

    private var mode = "chat"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_assist)

        val btnGrammar = findViewById<Button>(R.id.btnGrammar)
        val btnContext = findViewById<Button>(R.id.btnContext)
        val btnChat = findViewById<Button>(R.id.btnChat)
        val btnSend = findViewById<Button>(R.id.btnSend)
        val etInput = findViewById<EditText>(R.id.etInput)
        val tvOutput = findViewById<TextView>(R.id.tvOutput)

        btnGrammar.setOnClickListener {
            mode = "grammar"
            Toast.makeText(this, "Grammar Check selected", Toast.LENGTH_SHORT).show()
        }

        btnContext.setOnClickListener {
            mode = "context"
            Toast.makeText(this, "Context Help selected", Toast.LENGTH_SHORT).show()
        }

        btnChat.setOnClickListener {
            mode = "chat"
            Toast.makeText(this, "AI Chat selected", Toast.LENGTH_SHORT).show()
        }

        btnSend.setOnClickListener {
            val text = etInput.text.toString().trim()

            if (text.isEmpty()) {
                Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            tvOutput.text = when (mode) {
                "grammar" -> "Corrected sentence:\n$text"
                "context" -> "This sentence is talking about:\n$text"
                else -> generateAiReply(text)
            }
        }
    }

    private fun generateAiReply(input: String): String {
        return when {
            input.contains("hello", true) ->
                "Hello! How can I help you today?"

            input.contains("translate", true) ->
                "I can help you translate English to Tamil."

            input.contains("tamil", true) ->
                "Tamil is a beautiful classical language."

            else ->
                "I am still learning. Please ask a simple question."
        }
    }
}
