package llusca.migugoty.projecthub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SongListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var songAdapter: SongAdapter
    private lateinit var songDatabase: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_song_list, container, false)
    }
    private fun onSongSelected(song: Song) {
        val intent = Intent(context, MusicInicioIn::class.java).apply {
            putExtra("SONG_ID", song.id)
            putExtra("SONG_TITLE", song.title)
            putExtra("SONG_ARTIST", song.artist)
            putExtra("SONG_DATA", song.data) // Ruta del archivo de la canción
            putExtra("SONG_DATE_ADDED", song.dateAdded)
        }
        startActivity(intent)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view_songs)
        recyclerView.layoutManager = LinearLayoutManager(context)

        songDatabase = SongDatabase(requireContext())
        val songs = songDatabase.getAllSongs()

        songAdapter = SongAdapter(requireContext(), songs)
        recyclerView.adapter = songAdapter
    }
}
