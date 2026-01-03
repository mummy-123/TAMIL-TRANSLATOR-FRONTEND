package com.example.tamiltranslatoronboarding

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class DailyVocabularyActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var container: LinearLayout
    private lateinit var progress: ProgressBar
    private lateinit var txtProgress: TextView
    private val prefs by lazy { getSharedPreferences("daily_vocab", MODE_PRIVATE) }

    private val words = listOf(
        "Friend" to "நண்பன்", "Family" to "குடும்பம்", "Love" to "அன்பு",
        "Peace" to "அமைதி", "Hope" to "நம்பிக்கை", "Smile" to "புன்னகை",
        "Truth" to "உண்மை", "Brave" to "துணிவு", "Dream" to "கனவு"
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_vocabulary)

        container = findViewById(R.id.wordsContainer)
        progress = findViewById(R.id.progressBar)
        txtProgress = findViewById(R.id.txtProgress)

        tts = TextToSpeech(this, this)

        loadTodayWords()

        findViewById<Button>(R.id.btnQuiz).setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }
        findViewById<Button>(R.id.btnFlash).setOnClickListener {
            startActivity(Intent(this, QuickTranslateActivity::class.java))
        }

    }

    private fun loadTodayWords() {
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val savedDay = prefs.getString("day", "")

        if (today != savedDay) {
            val shuffled = words.shuffled().take(4)
            prefs.edit().apply {
                putString("day", today)
                putString("words", shuffled.joinToString("|") { it.first + "," + it.second })
                putInt("learned", 0)
                apply()
            }
        }

        val list = prefs.getString("words", "")!!.split("|")
        val learned = prefs.getInt("learned", 0)
        progress.max = 4
        progress.progress = learned
        txtProgress.text = "$learned/4"

        list.forEach {
            val parts = it.split(",")
            val view = LayoutInflater.from(this).inflate(R.layout.item_word, container, false)
            view.findViewById<TextView>(R.id.txtEnglish).text = parts[0]
            view.findViewById<TextView>(R.id.txtTamil).text = parts[1]
            view.findViewById<ImageView>(R.id.btnSpeak).setOnClickListener {
                speak(parts[0])
                speak(parts[1], Locale("ta","IN"))
            }
            container.addView(view)
        }
    }

    private fun speak(text: String, locale: Locale = Locale.US) {
        tts.language = locale
        tts.speak(text, TextToSpeech.QUEUE_ADD, null, null)
    }

    override fun onInit(status: Int) {}

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}