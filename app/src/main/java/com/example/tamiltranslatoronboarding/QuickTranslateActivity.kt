package com.example.tamiltranslatoronboarding

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.*

class QuickTranslateActivity : AppCompatActivity() {

    private var isEngToTamil = true
    private lateinit var translator: Translator
    private lateinit var txtOutput: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_translate)

        val edtInput = findViewById<EditText>(R.id.edtInput)
        txtOutput = findViewById(R.id.txtOutput)
        val btnToggle = findViewById<Button>(R.id.btnToggle)
        val btnTranslate = findViewById<Button>(R.id.btnTranslate)

        createTranslator()

        btnToggle.setOnClickListener {
            isEngToTamil = !isEngToTamil
            btnToggle.text = if (isEngToTamil) "English → Tamil" else "Tamil → English"
            edtInput.text.clear()
            txtOutput.text = ""
            createTranslator()
        }

        btnTranslate.setOnClickListener {
            val text = edtInput.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener

            txtOutput.text = "Translating..."

            translator.translate(text)
                .addOnSuccessListener { txtOutput.text = it }
                .addOnFailureListener { e ->
                    txtOutput.text = "Error: ${e.message}"
                }
        }
    }

    private fun createTranslator() {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(if (isEngToTamil) TranslateLanguage.ENGLISH else TranslateLanguage.TAMIL)
            .setTargetLanguage(if (isEngToTamil) TranslateLanguage.TAMIL else TranslateLanguage.ENGLISH)
            .build()

        translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                txtOutput.text = "Model ready. Type text."
            }
            .addOnFailureListener {
                txtOutput.text = "Model download failed. Check internet."
            }
    }

    override fun onDestroy() {
        translator.close()
        super.onDestroy()
    }
}