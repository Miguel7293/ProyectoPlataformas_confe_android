package llusca.migugoty.projecthub

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class vozinicio : AppCompatActivity() {

    private lateinit var btnMod: Button
    private lateinit var btnGrabaciones: Button
    private lateinit var micImage: ImageView
    private lateinit var timerTextView: TextView

    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var startTime = 0L
    private var timerHandler = android.os.Handler()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.RECORD_AUDIO] == true &&
            permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
            startRecording()
        } else {
            // Handle permission denial
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vozinicio)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnMod = findViewById(R.id.btnMod)
        btnGrabaciones = findViewById(R.id.btnGrabaciones)
        micImage = findViewById(R.id.micImage)
        timerTextView = findViewById(R.id.timerTextView)

        micImage.setOnClickListener {
            if (isRecording) {
                stopRecording()
            } else {
                checkPermissionsAndStartRecording()
            }
        }

        btnGrabaciones.setOnClickListener {
            // Navigate to a new activity that shows recordings
            val intent = Intent(this, RecordingsActivity::class.java)
            startActivity(intent)
        }

        btnMod.setOnClickListener {
            // Navigate to a new activity for applying modulators
            val intent = Intent(this, ModulatorActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkPermissionsAndStartRecording() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                startRecording()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) -> {
                // Show rationale and request permission
                requestPermissionLauncher.launch(arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            }
            else -> {
                // Directly request permission
                requestPermissionLauncher.launch(arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            }
        }
    }

    private fun startRecording() {
        val audioFile = createAudioFile()
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(audioFile.absolutePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                prepare()
                start()
                isRecording = true
                startTime = System.currentTimeMillis()
                timerHandler.post(timerRunnable)
                micImage.setImageResource(R.drawable.ic_grabando) // Cambiar el icono cuando está grabando
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            try {
                stop()
            } catch (e: RuntimeException) {
                // Handle stop failure
            }
            release()
            isRecording = false
            timerHandler.removeCallbacks(timerRunnable)
            micImage.setImageResource(R.drawable.ic_microphone) // Restaurar el icono original
        }
        mediaRecorder = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaRecorder?.release()
        mediaRecorder = null
    }

    private val timerRunnable: Runnable = object : Runnable {
        override fun run() {
            val elapsed = System.currentTimeMillis() - startTime
            val seconds = (elapsed / 1000) % 60
            val minutes = (elapsed / (1000 * 60)) % 60
            val hours = (elapsed / (1000 * 60 * 60)) % 24
            timerTextView.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            timerHandler.postDelayed(this, 1000)
        }
    }

    private fun createAudioFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        return File.createTempFile("AUDIO_${timestamp}_", ".3gp", storageDir)
    }
}
