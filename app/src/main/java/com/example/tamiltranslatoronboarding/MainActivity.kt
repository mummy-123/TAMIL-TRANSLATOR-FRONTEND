package com.example.tamiltranslatoronboarding

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.nl.translate.*

class MainActivity : AppCompatActivity() {

    private lateinit var translator: Translator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etEnglish = findViewById<EditText>(R.id.etEnglish)
        val tvTamil = findViewById<TextView>(R.id.tvTamil)
        val btnTranslate = findViewById<Button>(R.id.btnTranslate)

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.TAMIL)
            .build()

        translator = Translation.getClient(options)

        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                Toast.makeText(this, "Tamil model ready", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Model download failed", Toast.LENGTH_LONG).show()
            }

        btnTranslate.setOnClickListener {
            val text = etEnglish.text.toString().trim()

            if (text.isEmpty()) {
                Toast.makeText(this, "Enter English text", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            translator.translate(text)
                .addOnSuccessListener { result ->
                    tvTamil.text = result
                }
                .addOnFailureListener {
                    tvTamil.text = "Translation error"
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        translator.close()
    }
}
