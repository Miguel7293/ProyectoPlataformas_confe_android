package llusca.migugoty.projecthub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FolderListFragment : Fragment() {

    private lateinit var folderAdapter: FolderAdapter
    private lateinit var folderList: List<String> // Suponiendo que manejas carpetas por nombres

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_folder_list, container, false)

        // Inicializar la lista de carpetas aquí
        folderList = getFoldersFromDevice() // Método que obtiene las carpetas del dispositivo

        folderAdapter = FolderAdapter(folderList)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_folders)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = folderAdapter

        return view
    }

    // Método ficticio para obtener carpetas
    private fun getFoldersFromDevice(): List<String> {
        // Implementar lógica para obtener carpetas de música del dispositivo
        return listOf("Folder 1", "Folder 2", "Folder 3", "Folder 4")
    }
}
