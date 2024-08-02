package llusca.migugoty.projecthub

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var songDatabase: SongDatabase
    private var songList: List<Song> = emptyList()
    private var currentSongIndex = 0
    private var isPlaying = false

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        songDatabase = SongDatabase(this)
        songList = songDatabase.getAllSongs()

        mediaPlayer.setOnCompletionListener {
            playNextSong()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val action = it.getStringExtra("ACTION")
            val songIndex = it.getIntExtra("SONG_INDEX", -1)
            when (action) {
                "PLAY" -> if (songIndex != -1) playSong(songList[songIndex])
                "PAUSE" -> pauseSong()
                "NEXT" -> playNextSong()
                "PREVIOUS" -> playPreviousSong()
            }
        }
        return START_STICKY
    }

    private fun playSong(song: Song) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.reset()
        }

        mediaPlayer.setDataSource(song.data)
        mediaPlayer.prepare()
        mediaPlayer.start()
        isPlaying = true
        showNotification()
    }

    private fun pauseSong() {
        if (isPlaying && mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isPlaying = false
            showNotification()
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

    private fun showNotification() {
        val channelId = "music_service_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Music Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(this, MusicInicioIn::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Music Player")
            .setContentText("Playing music...")
            .setSmallIcon(R.drawable.ic_play) // Cambia esto a tu ícono de notificación
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        NotificationManagerCompat.from(this).notify(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
}
