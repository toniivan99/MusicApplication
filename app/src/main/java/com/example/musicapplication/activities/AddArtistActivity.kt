package com.example.musicapplication.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.musicapplication.R
import com.example.musicapplication.domain.Artist

class AddArtistActivity : BaseActivity() {
    private var nameEditText: EditText? = null
    private var genreEditText: EditText? = null
    private var addArtistButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_artist)
        nameEditText = findViewById(R.id.addArtistName)
        genreEditText = findViewById(R.id.addArtistGenre)
        addArtistButton = findViewById(R.id.addArtistButton)
        addArtistButton?.setOnClickListener(View.OnClickListener {
            try {
                val artist = Artist(nameEditText?.getText().toString(), genreEditText?.getText().toString())
                addArtist(artist)
                runOnUiThread {
                    Toast.makeText(applicationContext, "Artist added", Toast.LENGTH_SHORT).show()
                    nameEditText?.setText("")
                    genreEditText?.setText("")
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                runOnUiThread { Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show() }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { Toast.makeText(applicationContext, "Adding artist failed", Toast.LENGTH_SHORT).show() }
            }
        })
    }
}