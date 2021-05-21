package com.example.musicapplication.activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.example.musicapplication.R

class DeleteSongActivity : BaseActivity() {
    private var songsSpinner: Spinner? = null
    private var deleteSongButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_song)
        songsSpinner = findViewById(R.id.deleteSong)
        deleteSongButton = findViewById(R.id.deleteSongButton)
        fillSpinnerWithSongs()
        deleteSongButton?.setOnClickListener(View.OnClickListener {
            val thread = Thread {
                try {
                    val title = songsSpinner?.getSelectedItem().toString().split("-").toTypedArray()[1]
                    deleteSong(title)
                    fillSpinnerWithSongs()
                    runOnUiThread { Toast.makeText(applicationContext, "Song deleted", Toast.LENGTH_SHORT).show() }
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                    runOnUiThread { Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show() }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread { Toast.makeText(applicationContext, "Failed to delete the song", Toast.LENGTH_SHORT).show() }
                }
            }
            thread.start()
        })
    }

    private fun fillSpinnerWithSongs() {
        val thread = Thread {
            try {
                val songs = allSongs
                runOnUiThread {
                    val adapter = ArrayAdapter(this@DeleteSongActivity, android.R.layout.simple_list_item_1, android.R.id.text1, songs)
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