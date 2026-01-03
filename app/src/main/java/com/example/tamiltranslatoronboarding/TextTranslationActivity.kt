package com.example.tamiltranslatoronboarding

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.nl.translate.*
import android.speech.tts.TextToSpeech
import java.util.Locale

class TextTranslationActivity : AppCompatActivity() {

    private lateinit var etSourceText: EditText
    private lateinit var tvTamilText: TextView
    private lateinit var btnTranslate: Button
    private lateinit var ivSwap: ImageButton
    private lateinit var translator: Translator
    private lateinit var tts: TextToSpeech

    // true = English → Tamil
    // false = Tamil → English
    private var isEnglishToTamil = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_translation)

        // Bind views
        etSourceText = findViewById(R.id.etSourceText)
        tvTamilText = findViewById(R.id.tvTamilText)
        btnTranslate = findViewById(R.id.btnTranslate)
        ivSwap = findViewById(R.id.ivSwap)

        val ivSpeaker = findViewById<ImageView>(R.id.ivSpeaker)
        val ivCopy = findViewById<ImageView>(R.id.ivCopy)

        // Text To Speech init
        tts = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
            }
        }

        // Initial translator
        setupTranslator()

        btnTranslate.setOnClickListener { translateText() }

        ivSwap.setOnClickListener {
            animateSwap()
            swapLanguagesOnly()
        }

        // Speaker click
        ivSpeaker.setOnClickListener {
            val text = tvTamilText.text.toString().trim()
            if (text.isEmpty() || text == "Translation will appear here...") {
                Toast.makeText(this, "Nothing to speak", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val locale = if (isEnglishToTamil) {
                Locale("ta", "IN")
            } else {
                Locale.US
            }

            tts.language = locale
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TRANSLATION_TTS")
        }

        // Copy click
        ivCopy.setOnClickListener {
            val text = tvTamilText.text.toString()
            if (text.isNotEmpty() && text != "Translation will appear here...") {
                val clipboard =
                    getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
                clipboard.setPrimaryClip(
                    android.content.ClipData.newPlainText("translation", text)
                )
                Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupTranslator() {
        if (::translator.isInitialized) translator.close()

        val options = if (isEnglishToTamil) {
            TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.TAMIL)
                .build()
        } else {
            TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.TAMIL)
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build()
        }

        translator = Translation.getClient(options)
    }

    private fun swapLanguagesOnly() {
        isEnglishToTamil = !isEnglishToTamil
        setupTranslator()
        tvTamilText.text = "Translation will appear here..."
    }

    private fun translateText() {
        val inputText = etSourceText.text.toString().trim()
        if (inputText.isEmpty()) {
            Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show()
            return
        }

        tvTamilText.text = "Translating..."

        translator.translate(inputText)
            .addOnSuccessListener {
                tvTamilText.text = it
            }
            .addOnFailureListener {
                tvTamilText.text = "Translation failed"
            }
    }

    private fun animateSwap() {
        ivSwap.animate().rotationBy(180f).setDuration(300).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::translator.isInitialized) translator.close()
        if (::tts.isInitialized) tts.shutdown()
    }
}
