package com.example.tamiltranslatoronboarding

import android.content.Intent
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Quick Actions
        bindAction(R.drawable.ic_translate, "Text Translation", TextTranslationActivity::class.java)
        bindAction(R.drawable.ic_mic, "Voice Input", VoiceTranslationActivity::class.java)
        bindAction(R.drawable.ic_camera, "Camera Scan", CameraTranslationActivity::class.java)
        bindAction(R.drawable.ic_document, "Document", DocumentTranslationActivity::class.java)
        bindAction(R.drawable.ic_offline, "Offline Mode", OfflineModeActivity::class.java)
        bindAction(R.drawable.ic_ai, "AI Assist", AIChatActivity::class.java)

        // Learning Section
        bindLearning(R.drawable.ic_book, "Daily Vocabulary", "5 new senetences")
        bindLearning(R.drawable.ic_quiz, "Quick Quiz", "Test yourself")
        bindLearning(R.drawable.ic_streak, "Daily Streak", "Keep learning")
        bindLearning(R.drawable.ic_trophy, "Achievements", "Track progress")

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun bindAction(iconRes: Int, title: String, target: Class<*>) {
        val container = findViewById<GridLayout>(R.id.quickActionsContainer)

        val view = layoutInflater.inflate(R.layout.item_home_action, container, false)

        view.findViewById<ImageView>(R.id.imgIcon).setImageResource(iconRes)
        view.findViewById<TextView>(R.id.txtTitle).text = title

        view.setOnClickListener {
            startActivity(Intent(this, target))
        }

        container.addView(view)
    }

    private fun bindLearning(iconRes: Int, title: String, subtitle: String) {
        val container = findViewById<GridLayout>(R.id.learningContainer)

        val view = layoutInflater.inflate(R.layout.item_learning, container, false)

        view.findViewById<ImageView>(R.id.imgLearningIcon).setImageResource(iconRes)
        view.findViewById<TextView>(R.id.txtLearningTitle).text = title
        view.findViewById<TextView>(R.id.txtLearningSubtitle).text = subtitle

        view.setOnClickListener {
            when (title) {
                "Daily Vocabulary" ->
                    startActivity(Intent(this, DailySentencesActivity::class.java))

                "Quick Quiz" ->
                    startActivity(Intent(this, QuizActivity::class.java))

                "Daily Streak" ->
                    startActivity(Intent(this, QuickTranslateActivity::class.java))

                "Achievements" ->
                    startActivity(Intent(this, AIChatActivity::class.java))
            }
        }

        container.addView(view)
    }
}