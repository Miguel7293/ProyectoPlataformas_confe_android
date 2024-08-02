package llusca.migugoty.projecthub

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val data: String,
    val dateAdded: Long
)

class SongDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_SONGS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_ARTIST TEXT," +
                "$COLUMN_DATA TEXT," +
                "$COLUMN_DATE_ADDED INTEGER)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SONGS")
        onCreate(db)
    }

    fun addSong(song: Song) {
        val db = writableDatabase

        // Verificar si la canción ya existe
        val query = "SELECT COUNT(*) FROM $TABLE_SONGS WHERE $COLUMN_ID = ?"
        val cursor: Cursor? = db.rawQuery(query, arrayOf(song.id.toString()))
        cursor?.use {
            if (it.moveToFirst() && it.getInt(0) > 0) {
                // La canción ya existe, no hacer nada
                return
            }
        }

        // La canción no existe, realizar la inserción
        val values = ContentValues().apply {
            put(COLUMN_ID, song.id)
            put(COLUMN_TITLE, song.title)
            put(COLUMN_ARTIST, song.artist)
            put(COLUMN_DATA, song.data)
            put(COLUMN_DATE_ADDED, song.dateAdded)
        }
        db.insert(TABLE_SONGS, null, values)
        db.close()
    }

    fun clearAllSongs() {
        val db = writableDatabase
        db.delete(TABLE_SONGS, null, null)
        db.close()
    }

    fun getAllSongs(): List<Song> {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_SONGS, // Nombre de la tabla
            null, // Selecciona todas las columnas
            null, // Sin cláusula WHERE (obtiene todos los registros)
            null, // Sin argumentos de selección
            null, // Sin agrupamiento
            null, // Sin cláusula HAVING
            null // Sin cláusula ORDER BY (puedes especificar si deseas ordenar los resultados)
        )

        val songs = mutableListOf<Song>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)) ?: "Unknown Title"
            val artist = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ARTIST)) ?: "Unknown Artist"
            val data = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)) ?: "Unknown Data"
            val dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE_ADDED))
            songs.add(Song(id, title, artist, data, dateAdded))
        }
        cursor.close()
        db.close()
        return songs
    }



    companion object {
        private const val DATABASE_NAME = "songs.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_SONGS = "songs"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_ARTIST = "artist"
        const val COLUMN_DATA = "data"
        const val COLUMN_DATE_ADDED = "date_added"
    }
}
