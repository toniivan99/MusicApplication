package com.example.musicapplication.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.example.musicapplication.R
import com.example.musicapplication.domain.Artist

class EditArtistActivity : BaseActivity() {
    private var artistsSpinner: Spinner? = null
    private var nameEditText: EditText? = null
    private var genreEditText: EditText? = null
    private var editArtistButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_artist)
        artistsSpinner = findViewById(R.id.editArtists)
        nameEditText = findViewById(R.id.editArtistName)
        genreEditText = findViewById(R.id.editArtistMusicGenre)
        editArtistButton = findViewById(R.id.editArtistButton)
        fillSpinnerWithArtists()
        editArtistButton?.setOnClickListener(View.OnClickListener {
            val thread = Thread {
                try {
                    val artistName = artistsSpinner?.getSelectedItem().toString()
                    val artist = Artist(nameEditText?.getText().toString(), genreEditText?.getText().toString())
                    updateArtist(artistName, artist)
                    fillSpinnerWithArtists()
                    runOnUiThread {
                        nameEditText?.setText("")
                        genreEditText?.setText("")
                        Toast.makeText(applicationContext, "Artist updated", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                    runOnUiThread { Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show() }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread { Toast.makeText(applicationContext, "Failed to edit the artist", Toast.LENGTH_SHORT).show() }
                }
            }
            thread.start()
        })
        artistsSpinner?.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                val artistName = artistsSpinner?.getSelectedItem().toString()
                val artist = getArtist(artistName)
                nameEditText?.setText(artist.name)
                genreEditText?.setText(artist.musicGenre)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                nameEditText?.setText("")
                genreEditText?.setText("")
            }
        })
    }

    protected fun fillSpinnerWithArtists() {
        val thread = Thread {
            try {
                val artists = allArtists
                runOnUiThread {
                    val adapter = ArrayAdapter(this@EditArtistActivity, android.R.layout.simple_list_item_1, android.R.id.text1, artists)
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