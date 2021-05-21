package com.example.musicapplication.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.musicapplication.R
import java.util.*

class DeezerArtistActivity : BaseActivity() {
    private var nameEditText: EditText? = null
    private var searchArtistButton: Button? = null
    private var foundArtistEditText: TextView? = null
    private var foundTopTracksListView: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deezer_artist)
        nameEditText = findViewById(R.id.searchArtistName)
        searchArtistButton = findViewById(R.id.searchArtistButton)
        foundArtistEditText = findViewById(R.id.foundArtistName)
        foundTopTracksListView = findViewById(R.id.foundTopTracks)
        searchArtistButton?.setOnClickListener(View.OnClickListener {
            val name = nameEditText?.getText().toString()
            val capitalizedArtistName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase()
            foundArtistEditText?.setText("$capitalizedArtistName top tracks")
            val thread = Thread {
                try {
                    val artistData = getArtistFromDeezer(name)
                    val topTracks = getArtistTopTracks(artistData.getString("id"))
                    val topTrackNames: MutableList<String> = ArrayList()
                    for (i in 0..4) {
                        topTrackNames.add(topTracks.getJSONObject(i).getString("title"))
                    }
                    runOnUiThread {
                        val adapter = ArrayAdapter(this@DeezerArtistActivity, android.R.layout.simple_list_item_1, android.R.id.text1, topTrackNames)
                        foundTopTracksListView?.setAdapter(adapter)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread { Toast.makeText(applicationContext, "Failed to get artist information", Toast.LENGTH_LONG).show() }
                }
            }
            thread.start()
        })
    }
}