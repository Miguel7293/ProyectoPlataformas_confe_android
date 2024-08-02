package llusca.migugoty.projecthub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AlbumListFragment : Fragment() {

    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var albumList: List<Album> // Suponiendo que tienes una clase Album

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_album_list, container, false)

        // Inicializar la lista de álbumes aquí
        albumList = getAlbumsFromDevice() // Método que obtiene los álbumes del dispositivo

        albumAdapter = AlbumAdapter(albumList)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_albums)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = albumAdapter

        return view
    }

    // Método ficticio para obtener álbumes
    private fun getAlbumsFromDevice(): List<Album> {
        // Implementar lógica para obtener álbumes de música del dispositivo
        return listOf(
            Album("Album 1", "Artist 1"),
            Album("Album 2", "Artist 2"),
            Album("Album 3", "Artist 3")
        )
    }
}
