package llusca.migugoty.projecthub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlbumAdapter(private val albumList: List<Album>) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = albumList[position]
        holder.albumTitle.text = album.title
        holder.albumArtist.text = album.artist
    }

    override fun getItemCount() = albumList.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val albumTitle: TextView = view.findViewById(R.id.album_title)
        val albumArtist: TextView = view.findViewById(R.id.album_artist)
    }
}
