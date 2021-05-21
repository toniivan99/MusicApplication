package com.example.musicapplication.activities

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapplication.domain.Artist
import com.example.musicapplication.domain.Song
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

open class BaseActivity : AppCompatActivity() {
    @Throws(Exception::class)
    protected fun getArtistFromDeezer(name: String): JSONObject {
        var artistData = JSONObject()
        val deezerUrl = "http://api.deezer.com/search/artist?request_method=GET&q=" + name.trim().toLowerCase()
        val url = URL(deezerUrl)
        val http = url.openConnection() as HttpURLConnection
        http.doInput = true
        http.doOutput = true
        var result = ""
        val response = http.responseCode
        if (response == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(http.inputStream))
            var line = reader.readLine();
            while (line != null) {
                result += line + "\n";
                line = reader.readLine();
            }
            reader.close()
        } else {
            throw IOException("Error response code: $response")
        }
        val data = JSONObject(result)
        artistData = data.getJSONArray("data").getJSONObject(0)
        return artistData
    }

    @Throws(Exception::class)
    protected fun getArtistTopTracks(artistId: String): JSONArray {
        var topTracks = JSONArray()
        val deezerUrl = "http://api.deezer.com/artist/" + artistId.trim { it <= ' ' }.toLowerCase() + "/top?request_method=GET"
        val url = URL(deezerUrl)
        val http = url.openConnection() as HttpURLConnection
        http.doInput = true
        http.doOutput = true
        var result = ""
        val response = http.responseCode
        if (response == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(http.inputStream))
            var line = reader.readLine();
            while (line != null) {
                result += line + "\n";
                line = reader.readLine();
            }
            reader.close()
        } else {
            throw IOException("Error response code: $response")
        }
        val data = JSONObject(result)
        topTracks = data.getJSONArray("data")
        return topTracks
    }

    protected fun createDatabase() {
        val database = SQLiteDatabase.openOrCreateDatabase(File(filesDir.path + "/MusicApp.db"), null)
        database.execSQL("CREATE TABLE IF NOT EXISTS artists(id integer primary key autoincrement, name text not null unique, music_genre text not null);")
        database.execSQL("CREATE TABLE IF NOT EXISTS songs(id integer primary key autoincrement, title text not null unique, date text, duration integer);")
        database.execSQL("CREATE TABLE IF NOT EXISTS artists_songs(artistId integer not null, songId integer not null, FOREIGN KEY (artistId) REFERENCES artists(id), " +
                "FOREIGN KEY (songId) REFERENCES songs(id));")
        database.close()
    }

    protected fun addArtist(artist: Artist) {
        val database = SQLiteDatabase.openOrCreateDatabase(File(filesDir.path + "/MusicApp.db"), null)
        database.execSQL("INSERT INTO artists(name, music_genre) VALUES(?, ?)", arrayOf(artist.name, artist.musicGenre))
        database.close()
    }

    protected fun getArtist(name: String): Artist {
        val database = SQLiteDatabase.openOrCreateDatabase(File(filesDir.path + "/MusicApp.db"), null)
        val cursor = database.rawQuery("SELECT * FROM artists WHERE name = ?", arrayOf(name))
        return if (cursor.moveToNext()) {
            val artist = Artist()
            artist.id = cursor.getInt(cursor.getColumnIndex("id"))
            artist.name = cursor.getString(cursor.getColumnIndex("name"))
            artist.musicGenre = cursor.getString(cursor.getColumnIndex("music_genre"))
            database.close()
            artist
        } else {
            throw IllegalStateException("Artist doesn't exists")
        }
    }

    protected val allArtists: List<Artist>
        protected get() {
            val database = SQLiteDatabase.openOrCreateDatabase(File(filesDir.path + "/MusicApp.db"), null)
            val cursor = database.rawQuery("SELECT * FROM artists", arrayOf())
            val artists: MutableList<Artist> = ArrayList()
            while (cursor.moveToNext()) {
                val artist = Artist()
                artist.id = cursor.getInt(cursor.getColumnIndex("id"))
                artist.name = cursor.getString(cursor.getColumnIndex("name"))
                artist.musicGenre = cursor.getString(cursor.getColumnIndex("music_genre"))
                artists.add(artist)
            }
            database.close()
            return artists
        }

    protected fun updateArtist(name: String?, artist: Artist) {
        val database = SQLiteDatabase.openOrCreateDatabase(File(filesDir.path + "/MusicApp.db"), null)
        database.execSQL("UPDATE artists SET name = ?, music_genre = ? WHERE name = ?", arrayOf(artist.name, artist.musicGenre, name))
        database.close()
    }

    protected fun deleteArtist(name: String) {
        val database = SQLiteDatabase.openOrCreateDatabase(File(filesDir.path + "/MusicApp.db"), null)
        val artistsCursor = database.rawQuery("SELECT id FROM artists WHERE name = ?", arrayOf(name))
        if (artistsCursor.moveToNext()) {
            val id = artistsCursor.getInt(artistsCursor.getColumnIndex("id"))
            val artistsSongsCursor = database.rawQuery("SELECT COUNT(*) as count FROM artists_songs WHERE artistId = ?", arrayOf(id.toString()))
            artistsSongsCursor.moveToNext()
            val count = artistsSongsCursor.getInt(artistsSongsCursor.getColumnIndex("count"))
            if (count == 0) {
                database.execSQL("DELETE FROM artists WHERE id = ?", arrayOf(id))
            } else {
                throw IllegalStateException("There are songs connected with this artist")
            }
        } else {
            throw IllegalStateException("Artist doesn't exist")
        }
        database.close()
    }

    protected fun addSong(song: Song) {
        val database = SQLiteDatabase.openOrCreateDatabase(File(filesDir.path + "/MusicApp.db"), null)
        val cursor = database.rawQuery("SELECT id FROM artists WHERE name = ?", arrayOf(song.artist!!.name))
        if (cursor.moveToNext()) {
            song.artist!!.id = cursor.getInt(0)
        } else {
            throw IllegalArgumentException("Artist doesn't exists")
        }
        database.execSQL("INSERT INTO songs(title, date, duration) VALUES(?, ?, ?)", arrayOf(song.title, song.releasedDate, java.lang.String.valueOf(song.duration)))
        val songsCursor = database.rawQuery("SELECT id FROM songs WHERE title = ?", arrayOf(song.title))
        songsCursor.moveToNext()
        song.id = songsCursor.getInt(songsCursor.getColumnIndex("id"))
        database.execSQL("INSERT INTO artists_songs(artistId, songId) VALUES(?, ?)", arrayOf(song.artist!!.id, song.id))
        database.close()
    }

    protected fun getSongArtist(songId: Int): Artist {
        val database = SQLiteDatabase.openOrCreateDatabase(File(filesDir.path + "/MusicApp.db"), null)
        val artistsSongsCursor = database.rawQuery("SELECT artistId FROM artists_songs WHERE songId = ?", arrayOf(songId.toString()))
        artistsSongsCursor.moveToNext()
        val artistId = artistsSongsCursor.getInt(artistsSongsCursor.getColumnIndex("artistId"))
        val artistsCursor = database.rawQuery("SELECT name FROM artists WHERE id = ?", arrayOf(artistId.toString()))
        artistsCursor.moveToNext()
        val artistName = artistsCursor.getString(artistsCursor.getColumnIndex("name"))
        database.close()
        val artist = Artist()
        artist.id = artistId
        artist.name = artistName
        return artist
    }

    protected fun getSong(title: String): Song {
        val database = SQLiteDatabase.openOrCreateDatabase(File(filesDir.path + "/MusicApp.db"), null)
        val cursor = database.rawQuery("SELECT * FROM songs WHERE title = ?", arrayOf(title))
        return if (cursor.moveToNext()) {
            val songId = cursor.getInt(cursor.getColumnIndex("id"))
            val songTitle = cursor.getString(cursor.getColumnIndex("title"))
            val songDate = cursor.getString(cursor.getColumnIndex("date"))
            val songDuration = cursor.getInt(cursor.getColumnIndex("duration"))
            val songArtist = getSongArtist(songId)
            val song = Song()
            song.id = songId
            song.title = songTitle
            song.releasedDate = songDate
            song.duration = songDuration
            song.artist = songArtist
            database.close()
            song
        } else {
            throw IllegalStateException("Song doesn't exists")
        }
    }

    protected val allSongs: List<Song>
        protected get() {
            val database = SQLiteDatabase.openOrCreateDatabase(File(filesDir.path + "/MusicApp.db"), null)
            val cursor = database.rawQuery("SELECT * FROM songs", arrayOf())
            val songs: MutableList<Song> = ArrayList()
            while (cursor.moveToNext()) {
                val songId = cursor.getInt(cursor.getColumnIndex("id"))
                val songTitle = cursor.getString(cursor.getColumnIndex("title"))
                val songDate = cursor.getString(cursor.getColumnIndex("date"))
                val songDuration = cursor.getInt(cursor.getColumnIndex("duration"))
                val songArtist = getSongArtist(songId)
                val song = Song()
                song.id = songId
                song.title = songTitle
                song.releasedDate = songDate
                song.duration = songDuration
                song.artist = songArtist
                songs.add(song)
            }
            return songs
        }

    protected fun updateSong(title: String?, song: Song) {
        val database = SQLiteDatabase.openOrCreateDatabase(File(filesDir.path + "/MusicApp.db"), null)
        database.execSQL("UPDATE songs SET title = ?, date = ?, duration = ? WHERE title = ?", arrayOf(song.title, song.releasedDate,
                java.lang.String.valueOf(song.duration), title))
        database.close()
    }

    protected fun deleteSong(title: String) {
        val database = SQLiteDatabase.openOrCreateDatabase(File(filesDir.path + "/MusicApp.db"), null)
        val cursor = database.rawQuery("SELECT id FROM songs WHERE title = ?", arrayOf(title))
        if (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            database.execSQL("DELETE FROM artists_songs WHERE songId = ?", arrayOf(id))
            database.execSQL("DELETE FROM songs WHERE id = ?", arrayOf(id))
        } else {
            throw IllegalStateException("Song doesn't exist")
        }
        database.close()
    }
}