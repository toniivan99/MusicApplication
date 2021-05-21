package com.example.musicapplication.activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.example.musicapplication.R

class DeleteArtistActivity : BaseActivity() {
    private var artistsSpinner: Spinner? = null
    private var deleteArtistButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_artist)
        artistsSpinner = findViewById(R.id.deleteArtist)
        deleteArtistButton = findViewById(R.id.deleteArtistButton)
        fillSpinnerWithArtists()
        deleteArtistButton?.setOnClickListener(View.OnClickListener {
            val thread = Thread {
                try {
                    val name = artistsSpinner?.getSelectedItem().toString()
                    deleteArtist(name)
                    fillSpinnerWithArtists()
                    runOnUiThread { Toast.makeText(applicationContext, "Artist deleted", Toast.LENGTH_SHORT).show() }
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                    runOnUiThread { Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show() }
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                    runOnUiThread { Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show() }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread { Toast.makeText(applicationContext, "Failed to delete the artist", Toast.LENGTH_SHORT).show() }
                }
            }
            thread.start()
        })
    }

    protected fun fillSpinnerWithArtists() {
        val thread = Thread {
            try {
                val artists = allArtists
                runOnUiThread {
                    val adapter = ArrayAdapter(this@DeleteArtistActivity, android.R.layout.simple_list_item_1, android.R.id.text1, artists)
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