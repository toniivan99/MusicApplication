package com.example.musicapplication.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.example.musicapplication.R
import com.example.musicapplication.domain.Artist
import com.example.musicapplication.domain.Song
import java.lang.String

class EditSongActivity : BaseActivity() {
    private var songsSpinner: Spinner? = null
    private var titleEditText: EditText? = null
    private var dateEditText: EditText? = null
    private var durationEditText: EditText? = null
    private var editSongButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_song)
        songsSpinner = findViewById(R.id.editSong)
        titleEditText = findViewById(R.id.editSongTitle)
        dateEditText = findViewById(R.id.editSongDate)
        durationEditText = findViewById(R.id.editSongDuration)
        editSongButton = findViewById(R.id.editSongButton)
        fillSpinnerWithSongs()
        editSongButton?.setOnClickListener(View.OnClickListener {
            val thread = Thread {
                try {
                    val songTitle = songsSpinner?.getSelectedItem().toString().split("-").toTypedArray()[1]
                    val newTitle = titleEditText?.getText().toString()
                    val date = dateEditText?.getText().toString()
                    val duration = durationEditText?.getText().toString().toInt()
                    val song = Song(newTitle, date, duration, Artist())
                    updateSong(songTitle, song)
                    fillSpinnerWithSongs()
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Song updated", Toast.LENGTH_SHORT).show()
                        titleEditText?.setText("")
                        dateEditText?.setText("")
                        durationEditText?.setText("")
                    }
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                    runOnUiThread { Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show() }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread { Toast.makeText(applicationContext, "Failed to edit the song", Toast.LENGTH_SHORT).show() }
                }
            }
            thread.start()
        })
        songsSpinner?.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val songName = parent.selectedItem.toString().split("-").toTypedArray()[1]
                val song = getSong(songName)
                titleEditText?.setText(song.title)
                dateEditText?.setText(song.releasedDate)
                durationEditText?.setText(String.valueOf(song.duration))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                titleEditText?.setText("")
                dateEditText?.setText("")
                durationEditText?.setText("")
            }
        })
    }

    private fun fillSpinnerWithSongs() {
        val thread = Thread {
            try {
                val songs = allSongs
                runOnUiThread {
                    val adapter = ArrayAdapter(this@EditSongActivity, android.R.layout.simple_list_item_1, android.R.id.text1, songs)
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