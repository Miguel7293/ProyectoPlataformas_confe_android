package llusca.migugoty.projecthub

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class musicainicio : AppCompatActivity() {

    private lateinit var btnSelectSong: Button
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnFastForward: ImageButton
    private lateinit var btnRewind: ImageButton
    private lateinit var tvSongName: TextView
    private var mediaPlayer: MediaPlayer? = null
    private var songUriList: MutableList<Uri> = mutableListOf()
    private var currentSongIndex: Int = -1
    private val REQUEST_CODE_SELECT_SONG = 1
    private val REQUEST_CODE_PERMISSION = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_musicainicio)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnSelectSong = findViewById(R.id.btnSelectSong)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnFastForward = findViewById(R.id.btnFastForward)
        btnRewind = findViewById(R.id.btnRewind)
        tvSongName = findViewById(R.id.tvSongName)

        btnSelectSong.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
            } else {
                selectSongs()
            }
        }

        btnPlayPause.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                btnPlayPause.setImageResource(R.drawable.continuar)
            } else {
                mediaPlayer?.start()
                btnPlayPause.setImageResource(R.drawable.pause)
            }
        }

        btnFastForward.setOnClickListener {
            if (songUriList.isNotEmpty()) {
                currentSongIndex = (currentSongIndex + 1) % songUriList.size
                playSong(currentSongIndex)
            }
        }

        btnRewind.setOnClickListener {
            if (songUriList.isNotEmpty()) {
                currentSongIndex = if (currentSongIndex > 0) currentSongIndex - 1 else songUriList.size - 1
                playSong(currentSongIndex)
            }
        }
    }

    private fun selectSongs() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, REQUEST_CODE_SELECT_SONG)
    }

    private fun playSong(index: Int) {
        mediaPlayer?.release()
        songUriList.getOrNull(index)?.let { uri ->
            mediaPlayer = MediaPlayer.create(this, uri)
            mediaPlayer?.start()
            tvSongName.text = uri.lastPathSegment
            btnPlayPause.setImageResource(R.drawable.pause)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_SONG && resultCode == Activity.RESULT_OK) {
            songUriList.clear()
            data?.clipData?.let { clipData ->
                for (i in 0 until clipData.itemCount) {
                    songUriList.add(clipData.getItemAt(i).uri)
                }
            } ?: data?.data?.let { uri ->
                songUriList.add(uri)
            }
            if (songUriList.isNotEmpty()) {
                currentSongIndex = 0
                playSong(currentSongIndex)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectSongs()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
