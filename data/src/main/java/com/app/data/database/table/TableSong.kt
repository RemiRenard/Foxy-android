package com.app.data.database.table

import android.content.ContentValues
import com.app.data.model.Song

/**
 * Table Song of the local database, which contains mainly SQL
 */
object TableSong {

    val DATABASE_TABLE_NAME = "Song"
    val TABLE_SONG_ID = "id"
    val TABLE_SONG_URL = "url"
    val TABLE_SONG_NAME = "name"
    val TABLE_SONG_PICTURE = "picture"

    /**
     * Create the table song.
     * @return SQL String
     */
    fun createTableSong(): String {
        return "CREATE TABLE $DATABASE_TABLE_NAME (" +
                "$TABLE_SONG_ID TEXT NOT NULL PRIMARY KEY," +
                "$TABLE_SONG_URL TEXT NOT NULL," +
                "$TABLE_SONG_NAME TEXT NOT NULL," +
                "$TABLE_SONG_PICTURE TEXT NOT NULL)"
    }

    /**
     * Delete this table.
     */
    fun deleteTable(): String {
        return "DROP TABLE $DATABASE_TABLE_NAME"
    }

    /**
     * @param song Song?
     * @return Content values to create a song.
     */
    fun createSong(song: Song?): ContentValues {
        val values = ContentValues()
        values.put(TABLE_SONG_ID, song?.id)
        values.put(TABLE_SONG_URL, song?.url)
        values.put(TABLE_SONG_NAME, song?.name)
        values.put(TABLE_SONG_PICTURE, song?.picture)
        return values
    }

    /**
     * Get songs from database.
     * @return SQL String
     */
    fun getSongs(): String = "SELECT * FROM $DATABASE_TABLE_NAME"
}