package llusca.migugoty.projecthub

import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

class musicainicio : AppCompatActivity() {

    private lateinit var songDatabase: SongDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musicainicio)

        songDatabase = SongDatabase(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            scanAndAddSongs()
        }

        // Cargar el fragmento de la lista de canciones al iniciar la actividad
        openFragment(SongListFragment())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanAndAddSongs()
            } else {
                Toast.makeText(this, "El permiso es necesario para acceder a las canciones.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun scanAndAddSongs() {
        Thread {
            val contentResolver: ContentResolver = contentResolver
            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DATE_ADDED
            )

            val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)

            cursor?.use {
                if (it.moveToFirst()) {
                    do {
                        val idIndex = it.getColumnIndex(MediaStore.Audio.Media._ID)
                        val titleIndex = it.getColumnIndex(MediaStore.Audio.Media.TITLE)
                        val artistIndex = it.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                        val dataIndex = it.getColumnIndex(MediaStore.Audio.Media.DATA)
                        val dateAddedIndex = it.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)

                        if (idIndex != -1 && titleIndex != -1 && artistIndex != -1 && dataIndex != -1 && dateAddedIndex != -1) {
                            val id = it.getLong(idIndex)
                            val title = it.getString(titleIndex)
                            val artist = it.getString(artistIndex)
                            val data = it.getString(dataIndex)
                            val dateAdded = it.getLong(dateAddedIndex)

                            val song = Song(id, title, artist, data, dateAdded)
                            songDatabase.addSong(song)

                            Log.d("Musicainicio", "Added song to database: $song")
                        }
                    } while (it.moveToNext())
                }
                notifyFragmentSongsUpdated()
            }
        }.start()
    }

    private fun notifyFragmentSongsUpdated() {
        val intent = Intent("SONGS_UPDATED")
        sendBroadcast(intent)
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
    private fun onSongSelected(song: Song) {
        val intent = Intent(this, MusicInicioIn::class.java).apply {
            putExtra("SONG_ID", song.id)
            putExtra("SONG_TITLE", song.title)
            putExtra("SONG_DATA", song.data)
            // Puedes pasar más detalles si es necesario
        }
        startActivity(intent)
    }

}
