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

class DailySentencesActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var container: LinearLayout
    private lateinit var progress: ProgressBar
    private lateinit var txtProgress: TextView

    private val prefs by lazy {
        getSharedPreferences("daily_sentences", MODE_PRIVATE)
    }

    // ✅ ONLY FULL SENTENCES (NO WORDS)
    private val sentences = listOf(
        "Good morning, have a nice day" to "காலை வணக்கம், நல்ல நாளாக இருக்கட்டும்",
        "How are you feeling today?" to "இன்று நீங்கள் எப்படி உணர்கிறீர்கள்?",
        "I am learning Tamil step by step" to "நான் தமிழ் மெதுவாக கற்றுக்கொண்டு இருக்கிறேன்",
        "Please take care of yourself" to "தயவு செய்து உங்களை கவனித்துக்கொள்ளுங்கள்",
        "Thank you for your valuable time" to "உங்கள் மதிப்புமிக்க நேரத்திற்கு நன்றி",
        "Let us work hard to achieve our goals" to "நமது இலக்குகளை அடைய நாம் கடினமாக உழைப்போம்",
        "Every day is a new opportunity to learn" to "ஒவ்வொரு நாளும் கற்றுக்கொள்ள புதிய வாய்ப்பு",
        "Stay positive and keep smiling" to "நலமாக இருந்து புன்னகையை தொடருங்கள்"
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_sentences)

        container = findViewById(R.id.sentencesContainer)
        progress = findViewById(R.id.progressBar)
        txtProgress = findViewById(R.id.txtProgress)

        tts = TextToSpeech(this, this)

        loadTodaySentence()

        findViewById<Button>(R.id.btnQuiz).setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }

        findViewById<Button>(R.id.btnFlash).setOnClickListener {
            startActivity(Intent(this, QuickTranslateActivity::class.java))
        }
    }

    // ✅ ONLY ONE SENTENCE PER DAY, NO REPEAT
    private fun loadTodaySentence() {

        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val savedDay = prefs.getString("day", "")
        var index = prefs.getInt("index", 0)

        if (today != savedDay) {
            if (index >= sentences.size) index = 0  // reset after list ends

            prefs.edit().apply {
                putString("day", today)
                putInt("index", index + 1)
                putString(
                    "sentence",
                    sentences[index].first + "|" + sentences[index].second
                )
                apply()
            }
        }

        val data = prefs.getString("sentence", "") ?: return
        if (data.isEmpty()) return

        val parts = data.split("|")

        progress.max = 1
        progress.progress = 1
        txtProgress.text = "1/1"

        container.removeAllViews()

        val view = LayoutInflater.from(this)
            .inflate(R.layout.item_sentence, container, false)

        view.findViewById<TextView>(R.id.txtEnglish).text = parts[0]
        view.findViewById<TextView>(R.id.txtTamil).text = parts[1]

        view.findViewById<ImageView>(R.id.btnSpeak).setOnClickListener {
            speak(parts[0])
            speak(parts[1], Locale("ta", "IN"))
        }

        container.addView(view)
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
