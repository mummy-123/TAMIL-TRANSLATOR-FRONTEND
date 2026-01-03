package com.example.tamiltranslatoronboarding

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.*

class AIChatActivity : AppCompatActivity() {

    private val chat = mutableListOf<Pair<String, Boolean>>()
    private lateinit var adapter: ChatAdapter
    private lateinit var translator: Translator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_chat)

        val list = findViewById<RecyclerView>(R.id.chatList)
        val edt = findViewById<EditText>(R.id.edtMessage)
        val btn = findViewById<ImageButton>(R.id.btnSend)

        adapter = ChatAdapter(chat)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        chat.add("Hello! 😊 I’m your AI assistant. Ask me anything — I’ll reply like a human and help you learn Tamil too." to false)
        adapter.notifyItemInserted(0)

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.TAMIL)
            .build()

        translator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder().requireWifi().build()
        translator.downloadModelIfNeeded(conditions)

        btn.setOnClickListener {
            val q = edt.text.toString().trim()
            if (q.isEmpty()) return@setOnClickListener

            chat.add(q to true)
            adapter.notifyItemInserted(chat.size - 1)
            edt.text.clear()
            list.scrollToPosition(chat.size - 1)

            generateAIReply(q)
        }
    }

    private fun generateAIReply(userText: String) {

        val englishReply = when {
            userText.contains("hello", true) ->
                "Hello! 😊 Nice to meet you."

            userText.contains("how are you", true) ->
                "I’m doing great! Thanks for asking. 😊"

            userText.contains("what is", true) ->
                "That’s a good question! Let me explain it simply."

            userText.contains("help", true) ->
                "Sure! I’m here to help you. Tell me what you need."

            else ->
                "That’s interesting! Let’s learn how to say this in Tamil."
        }

        translator.translate(userText)
            .addOnSuccessListener { tamil ->
                val finalReply =
                    "$englishReply\n\nIn Tamil, you can say:\n$tamil"

                chat.add(finalReply to false)
                adapter.notifyItemInserted(chat.size - 1)
            }
            .addOnFailureListener {
                chat.add("Sorry 😔 I couldn’t understand that. Please try again." to false)
                adapter.notifyItemInserted(chat.size - 1)
            }
    }

    override fun onDestroy() {
        translator.close()
        super.onDestroy()
    }
}