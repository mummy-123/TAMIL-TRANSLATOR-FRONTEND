package com.example.tamiltranslatoronboarding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.InputStream
import android.widget.TextView

class DocumentTranslationActivity : AppCompatActivity() {

    private lateinit var btnChoose: Button
    private lateinit var txtTranslatedOutput: TextView
    private val PICK_PDF = 301

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_translation)

        PDFBoxResourceLoader.init(applicationContext)

        btnChoose = findViewById(R.id.btnChooseFile)
        txtTranslatedOutput = findViewById(R.id.txtTranslatedOutput)

        btnChoose.setOnClickListener { pickPDF() }
    }

    private fun pickPDF() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(intent, PICK_PDF)
    }

    override fun onActivityResult(rc: Int, res: Int, data: Intent?) {
        super.onActivityResult(rc, res, data)

        if (rc == PICK_PDF && res == RESULT_OK && data != null) {
            val uri = data.data ?: return
            extractAndTranslate(uri)
        }
    }

    private fun extractAndTranslate(uri: Uri) {
        Thread {
            val input = contentResolver.openInputStream(uri) ?: return@Thread
            val doc = PDDocument.load(input)
            val text = PDFTextStripper().getText(doc)
            doc.close()

            runOnUiThread {
                val identifier = LanguageIdentification.getClient()
                identifier.identifyLanguage(text)
                    .addOnSuccessListener { lang ->

                        val sourceLang = when {
                            lang == "ta" || lang.startsWith("ta") -> TranslateLanguage.TAMIL
                            lang == "en" || lang.startsWith("en") -> TranslateLanguage.ENGLISH
                            else -> TranslateLanguage.ENGLISH   // 🔐 fallback ONLY ENGLISH
                        }

                        val targetLang =
                            if (sourceLang == TranslateLanguage.TAMIL)
                                TranslateLanguage.ENGLISH
                            else
                                TranslateLanguage.TAMIL

                        translateText(text, sourceLang, targetLang)
                    }
            }
        }.start()
    }

    private fun translateText(text: String, from: String, to: String) {

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(from)
            .setTargetLanguage(to)
            .build()

        val translator = Translation.getClient(options)

        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                translator.translate(text)
                    .addOnSuccessListener { translatedText ->
                        txtTranslatedOutput.text = translatedText   // show result
                    }
                    .addOnFailureListener {
                        txtTranslatedOutput.text = "Translation failed"
                    }
            }
            .addOnFailureListener {
                txtTranslatedOutput.text = "Model download failed"
            }
    }

}