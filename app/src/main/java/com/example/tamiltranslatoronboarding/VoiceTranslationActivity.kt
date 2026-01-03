package com.example.tamiltranslatoronboarding

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.Locale

class VoiceTranslationActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var btnMic: ImageButton
    private lateinit var btnSpeak: ImageView
    private lateinit var txtSpoken: TextView
    private lateinit var txtTranslated: TextView
    private lateinit var txtDetectedLang: TextView
    private lateinit var btnMode: Button

    private var tamilToEnglish = false
    private lateinit var tts: TextToSpeech

    private val VOICE_REQ = 101
    private val AUDIO_PERMISSION = 201

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_translation)

        btnMic = findViewById(R.id.btnMic)
        btnSpeak = findViewById(R.id.btnSpeak)
        txtSpoken = findViewById(R.id.txtSpoken)
        txtTranslated = findViewById(R.id.txtTranslated)
        txtDetectedLang = findViewById(R.id.txtDetectedLang)
        btnMode = findViewById(R.id.btnMode)

        tts = TextToSpeech(this, this)

        btnMode.setOnClickListener {
            tamilToEnglish = !tamilToEnglish
            btnMode.text = if (tamilToEnglish) "Tamil ➜ English" else "English ➜ Tamil"
            txtDetectedLang.text = if (tamilToEnglish) " Tamil" else " English"
        }

        btnMic.setOnClickListener { startListening() }
        btnSpeak.setOnClickListener { speakText() }
    }

    private fun startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), AUDIO_PERMISSION)
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

        if (tamilToEnglish) {
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ta-IN")
        } else {
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        }

        startActivityForResult(intent, VOICE_REQ)
    }

    override fun onActivityResult(req: Int, res: Int, data: Intent?) {
        super.onActivityResult(req, res, data)

        if (req == VOICE_REQ && res == RESULT_OK && data != null) {
            val spoken = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)!![0]
            txtSpoken.text = spoken

            if (tamilToEnglish)
                translate(spoken, TranslateLanguage.TAMIL, TranslateLanguage.ENGLISH)
            else
                translate(spoken, TranslateLanguage.ENGLISH, TranslateLanguage.TAMIL)
        }
    }

    private fun translate(text: String, from: String, to: String) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(from)
            .setTargetLanguage(to)
            .build()

        val translator = Translation.getClient(options)
        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                translator.translate(text)
                    .addOnSuccessListener {
                        txtTranslated.text = it
                    }
            }
    }

    private fun speakText() {
        val text = txtTranslated.text.toString()
        tts.language = if (tamilToEnglish) Locale.ENGLISH else Locale("ta")
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onInit(status: Int) {}
}