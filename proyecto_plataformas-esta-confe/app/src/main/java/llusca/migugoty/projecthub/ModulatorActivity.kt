package llusca.migugoty.projecthub

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class ModulatorActivity : AppCompatActivity() {

    private lateinit var btnPlayOriginal: Button
    private lateinit var btnApplyModulator: Button

    private var audioFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modulator)

        btnPlayOriginal = findViewById(R.id.btnPlayOriginal)
        btnApplyModulator = findViewById(R.id.btnApplyModulator)

        audioFile = getLastRecordedFile()

        btnPlayOriginal.setOnClickListener {
            audioFile?.let {
                playAudio(it)
            } ?: Toast.makeText(this, "No recording found", Toast.LENGTH_SHORT).show()
        }

        btnApplyModulator.setOnClickListener {
            audioFile?.let {
                applyVoiceModulator(it)
            } ?: Toast.makeText(this, "No recording found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLastRecordedFile(): File? {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val files = storageDir?.listFiles()?.filter { it.extension == "3gp" }
        return files?.maxByOrNull { it.lastModified() }
    }

    private fun playAudio(file: File) {
        try {
            val audioStream = FileInputStream(file)
            val audioData = ByteArray(file.length().toInt())
            audioStream.read(audioData)
            audioStream.close()

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(44100)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(audioData.size)
                .build()

            audioTrack.play()
            audioTrack.write(audioData, 0, audioData.size)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun applyVoiceModulator(file: File) {
        // Aquí deberías implementar la lógica para aplicar un modulador de voz al archivo de audio
        // Este es un ejemplo simplificado, en la práctica necesitarás usar una biblioteca de procesamiento de audio
        Toast.makeText(this, "Modulator applied (example)", Toast.LENGTH_SHORT).show()
    }
}
