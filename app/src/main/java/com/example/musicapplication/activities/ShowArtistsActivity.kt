package com.example.musicapplication.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.musicapplication.R

class ShowArtistsActivity : BaseActivity() {
    private var artistsSpinner: Spinner? = null
    private var showArtistButton: Button? = null
    private var nameEditText: TextView? = null
    private var genreEditText: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_artists)
        artistsSpinner = findViewById(R.id.showArtists)
        showArtistButton = findViewById(R.id.showArtistButton)
        nameEditText = findViewById(R.id.showArtistName)
        genreEditText = findViewById(R.id.showArtistGenre)
        fillSpinnerWithArtists()
        showArtistButton?.setOnClickListener(View.OnClickListener {
            try {
                val name = artistsSpinner?.getSelectedItem().toString()
                val artist = getArtist(name)
                nameEditText?.setText("Artist name: " + artist.name)
                genreEditText?.setText("Artist music genre: " + artist.musicGenre)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                runOnUiThread { Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show() }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { Toast.makeText(applicationContext, "Failed to show artist information", Toast.LENGTH_LONG).show() }
            }
        })
    }

    protected fun fillSpinnerWithArtists() {
        val thread = Thread {
            try {
                val artists = allArtists
                runOnUiThread {
                    val adapter = ArrayAdapter(this@ShowArtistsActivity, android.R.layout.simple_list_item_1, android.R.id.text1, artists)
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