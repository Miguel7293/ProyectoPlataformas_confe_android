package llusca.migugoty.projecthub

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import llusca.migugoty.projecthub.databinding.ActivityMusicInicioInBinding

class MusicInicioIn : AppCompatActivity() {

    private lateinit var binding: ActivityMusicInicioInBinding
    private lateinit var mediaPlayer: MediaPlayer
    private var songList: List<Song> = emptyList()
    private var currentSongIndex: Int = 0
    private var isPlaying: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicInicioInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayer = MediaPlayer()

        val songId = intent.getLongExtra("SONG_ID", -1)
        val songTitle = intent.getStringExtra("SONG_TITLE")
        val songData = intent.getStringExtra("SONG_DATA")// Inicializar mediaPlayer

        if (songData != null) {
            // Crear el objeto Song con los datos recibidos
            val song = Song(songId, songTitle ?: "", "", songData, 0)
            playSong(song)
        } else {
            Log.e("MusicInicioIn", "No se recibió la ruta de la canción")
        }

        loadSongs()
        setupUI()
    }

    private fun loadSongs() {
        songList = obtenerListaDeCanciones()
        // Si hay canciones, reproducir la primera

    }

    private fun obtenerListaDeCanciones(): List<Song> {
        // Implementa la lógica para obtener la lista de canciones de la base de datos
        val songDatabase = SongDatabase(this)
        return songDatabase.getAllSongs()
    }

    private fun playSong(song: Song) {
        try {
            // Liberar el MediaPlayer actual si está en uso
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.reset()
            } else {
                mediaPlayer.reset()
            }

            // Configurar la fuente de datos y preparar el MediaPlayer
            mediaPlayer.apply {
                setDataSource(song.data) // Asegúrate de que esta ruta sea correcta
                setOnPreparedListener {
                    start()
                    this@MusicInicioIn.isPlaying = true
                    updateUI()
                }
                setOnErrorListener { mp, what, extra ->
                    Log.e("MusicInicioIn", "Error en MediaPlayer: what=$what, extra=$extra")
                    false
                }
                prepareAsync() // Preparar de forma asíncrona para evitar bloqueos
            }
        } catch (e: Exception) {
            Log.e("MusicInicioIn", "Error reproduciendo canción: ${e.message}")
        }
    }


    private fun pauseSong() {
        if (isPlaying && mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isPlaying = false
            updateUI()
        }
    }

    private fun playNextSong() {
        if (songList.isNotEmpty()) {
            currentSongIndex = (currentSongIndex + 1) % songList.size
            playSong(songList[currentSongIndex])
        }
    }

    private fun playPreviousSong() {
        if (songList.isNotEmpty()) {
            currentSongIndex = (currentSongIndex - 1 + songList.size) % songList.size
            playSong(songList[currentSongIndex])
        }
    }

    private fun setupUI() {
        binding.btnPlay.setOnClickListener {
            if (isPlaying) {
                pauseSong()
            } else {
                if (songList.isNotEmpty()) {
                    playSong(songList[currentSongIndex])
                }
            }
        }

        binding.btnPause.setOnClickListener {
            pauseSong() // Si el botón de pausa se presiona, pausa la canción actual
        }

        binding.btnNext.setOnClickListener {
            playNextSong()
        }

        binding.btnPrevious.setOnClickListener {
            playPreviousSong()
        }
    }

    private fun updateUI() {
        // Actualizar la interfaz de usuario, por ejemplo, el estado del botón de play/pausa
        if (songList.isNotEmpty()) {
            binding.songTitle.text = songList[currentSongIndex].title
        }

        if (isPlaying) {
            binding.btnPlay.visibility = View.INVISIBLE
            binding.btnPause.visibility = View.VISIBLE
        } else {
            binding.btnPlay.visibility = View.VISIBLE
            binding.btnPause.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }

}
