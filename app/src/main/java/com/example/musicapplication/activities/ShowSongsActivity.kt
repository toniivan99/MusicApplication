package com.example.musicapplication.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.musicapplication.R

class ShowSongsActivity : BaseActivity() {
    private var songsSpinner: Spinner? = null
    private var showSongButton: Button? = null
    private var titleTextView: TextView? = null
    private var dateTextView: TextView? = null
    private var durationTextView: TextView? = null
    private var artistTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_songs)
        songsSpinner = findViewById(R.id.showSongs)
        showSongButton = findViewById(R.id.showSongButton)
        titleTextView = findViewById(R.id.showSongTitle)
        dateTextView = findViewById(R.id.showSongDate)
        durationTextView = findViewById(R.id.showSongDuration)
        artistTextView = findViewById(R.id.showSongArtistName)
        fillSpinnerWithSongs()
        showSongButton?.setOnClickListener(View.OnClickListener {
            try {
                val title = songsSpinner?.getSelectedItem().toString().split("-").toTypedArray()[1]
                val song = getSong(title)
                titleTextView?.setText("Title: " + song.title)
                dateTextView?.setText("Released date: " + song.releasedDate)
                durationTextView?.setText("Duration: " + song.duration)
                artistTextView?.setText("Artist name: " + song.artist!!.name)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                runOnUiThread { Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show() }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { Toast.makeText(applicationContext, "Failed to show song information", Toast.LENGTH_LONG).show() }
            }
        })
    }

    private fun fillSpinnerWithSongs() {
        val thread = Thread {
            try {
                val songs = allSongs
                runOnUiThread {
                    val adapter = ArrayAdapter(this@ShowSongsActivity, android.R.layout.simple_list_item_1, android.R.id.text1, songs)
                    songsSpinner!!.adapter = adapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { Toast.makeText(applicationContext, "Couldn't get the list of songs", Toast.LENGTH_LONG).show() }
            }
        }
        thread.start()
    }
}