package com.example.musicapplication.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.musicapplication.R
import com.example.musicapplication.domain.Song

class AddSongActivity : BaseActivity() {
    private var titleEditText: EditText? = null
    private var dateEditText: EditText? = null
    private var durationEditText: EditText? = null
    private var artistsSpinner: Spinner? = null
    private var addSongButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_song)
        titleEditText = findViewById(R.id.addSongTitle)
        dateEditText = findViewById(R.id.addSongDate)
        durationEditText = findViewById(R.id.addSongDuration)
        artistsSpinner = findViewById(R.id.addSongArtist)
        addSongButton = findViewById(R.id.addSongButton)
        fillSpinnerWithArtists()
        addSongButton?.setOnClickListener(View.OnClickListener {
            try {
                val title = titleEditText?.getText().toString()
                val date = dateEditText?.getText().toString()
                val duration = durationEditText?.getText().toString().toInt()
                val artist = getArtist(artistsSpinner?.getSelectedItem().toString())
                val song = Song(title, date, duration, artist)
                addSong(song)
                runOnUiThread {
                    titleEditText?.setText("")
                    dateEditText?.setText("")
                    durationEditText?.setText("")
                    Toast.makeText(applicationContext, "Song added", Toast.LENGTH_LONG).show()
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                runOnUiThread { Toast.makeText(applicationContext, "Invalid duration", Toast.LENGTH_LONG).show() }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                runOnUiThread { Toast.makeText(applicationContext, "Artist doesn't exists", Toast.LENGTH_LONG).show() }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                runOnUiThread { Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show() }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { Toast.makeText(applicationContext, "Adding song failed", Toast.LENGTH_LONG).show() }
            }
        })
    }

    protected fun fillSpinnerWithArtists() {
        val thread = Thread {
            try {
                val artists = allArtists
                runOnUiThread {
                    val adapter = ArrayAdapter(this@AddSongActivity, android.R.layout.simple_list_item_1, android.R.id.text1, artists)
                    artistsSpinner!!.adapter = adapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { Toast.makeText(applicationContext, "Couldn't get the list of artists", Toast.LENGTH_LONG).show() }
            }
        }
        thread.start()
    }
}