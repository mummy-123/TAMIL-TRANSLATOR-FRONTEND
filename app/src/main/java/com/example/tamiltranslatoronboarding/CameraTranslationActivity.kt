package com.example.tamiltranslatoronboarding

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Size
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File

class CameraTranslationActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var txtResult: TextView
    private lateinit var btnSwitch: Button
    private lateinit var btnGallery: Button
    private lateinit var btnInstant: Button
    private lateinit var btnCapture: FrameLayout
    private lateinit var btnStop: Button

    private var tamilToEnglish = false
    private var isLiveMode = false
    private val PICK_IMAGE = 2001
    private val CAMERA_PERMISSION = 101
    private val CAPTURE_IMAGE = 3001
    private var photoUri: Uri? = null
    private var liveRunning = false

    private lateinit var imageCapture: ImageCapture
    private lateinit var imageAnalyzer: ImageAnalysis
    private lateinit var cameraProvider: ProcessCameraProvider

    private lateinit var translator: Translator
    private var translatorReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_translation)

        previewView = findViewById(R.id.cameraPreview)
        txtResult = findViewById(R.id.txtResult)
        btnSwitch = findViewById(R.id.btnSwitch)
        btnGallery = findViewById(R.id.btnGallery)
        btnInstant = findViewById(R.id.btnInstant)
        btnCapture = findViewById(R.id.btnCapture)
        btnStop = findViewById(R.id.btnStop)

        setupTranslator()

        btnSwitch.setOnClickListener {
            tamilToEnglish = !tamilToEnglish
            setupTranslator()
            btnSwitch.text = if (tamilToEnglish) "English ➜ Tamil" else "Tamil ➜ English"
        }

        btnInstant.setOnClickListener {
            if (!liveRunning) {
                isLiveMode = true
                startLiveCameraMode()
                liveRunning = true
                txtResult.text = "Live translation started"
            }
        }

        btnStop.setOnClickListener { stopLiveTranslation() }
        btnGallery.setOnClickListener { pickFromGallery() }
        btnCapture.setOnClickListener { capturePhoto() }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
        }
    }

    private fun stopLiveTranslation() {
        if (!::cameraProvider.isInitialized) return
        cameraProvider.unbindAll()
        liveRunning = false
        isLiveMode = false
        txtResult.text = "Live translation stopped"
    }

    private fun startLiveCameraMode() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()   // 🔥 FIXED (no shadowing)

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val analyzer = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            analyzer.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                if (!isLiveMode) {
                    imageProxy.close()
                    return@setAnalyzer
                }

                val mediaImage = imageProxy.image ?: run {
                    imageProxy.close()
                    return@setAnalyzer
                }

                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

                recognizer.process(image)
                    .addOnSuccessListener {
                        if (it.text.isNotEmpty()) translateText(it.text)
                    }
                    .addOnCompleteListener { imageProxy.close() }
            }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview, analyzer)

        }, ContextCompat.getMainExecutor(this))
    }

    private fun setupTranslator() {
        val options = if (tamilToEnglish)
            TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.TAMIL).build()
        else
            TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.TAMIL)
                .setTargetLanguage(TranslateLanguage.ENGLISH).build()

        if (::translator.isInitialized) translator.close()
        translator = Translation.getClient(options)
        translator.downloadModelIfNeeded().addOnSuccessListener {
            translatorReady = true
        }
    }

    private fun capturePhoto() {
        isLiveMode = false
        val file = File.createTempFile("capture_", ".jpg", externalCacheDir)
        photoUri = FileProvider.getUriForFile(this, "$packageName.provider", file)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, CAPTURE_IMAGE)
    }

    private fun pickFromGallery() {
        isLiveMode = false
        startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), PICK_IMAGE)
    }

    override fun onActivityResult(rc: Int, res: Int, data: Intent?) {
        super.onActivityResult(rc, res, data)

        if (rc == PICK_IMAGE && res == RESULT_OK) {
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data?.data!!)
            recognizeAndTranslate(InputImage.fromBitmap(bitmap, 0))
        }

        if (rc == CAPTURE_IMAGE && res == RESULT_OK) {
            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(photoUri!!))
            recognizeAndTranslate(InputImage.fromBitmap(bitmap, 0))
        }
    }

    private fun recognizeAndTranslate(image: InputImage) {
        if (!translatorReady) return
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(image)
            .addOnSuccessListener { if (it.text.isNotEmpty()) translateText(it.text) }
    }

    private fun translateText(text: String) {
        if (!translatorReady) return
        translator.translate(text).addOnSuccessListener { txtResult.text = it }
    }
}