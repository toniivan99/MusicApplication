package com.example.musicapplication.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.musicapplication.R

class MainActivity : BaseActivity() {
    private var addArtistButton: Button? = null
    private var getArtistsButton: Button? = null
    private var editArtistButton: Button? = null
    private var deleteArtistButton: Button? = null
    private var searchDeezerArtistButton: Button? = null
    private var addSongButton: Button? = null
    private var getSongsButton: Button? = null
    private var editSongButton: Button? = null
    private var deleteSongButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.INTERNET), 200)
        }
        createDatabase()
        addArtistButton = findViewById(R.id.addArtistMenu)
        getArtistsButton = findViewById(R.id.getArtistsMenu)
        editArtistButton = findViewById(R.id.editArtistMenu)
        deleteArtistButton = findViewById(R.id.deleteArtistMenu)
        searchDeezerArtistButton = findViewById(R.id.searchArtistMenu)
        addSongButton = findViewById(R.id.addSongMenu)
        getSongsButton = findViewById(R.id.getSongsMenu)
        editSongButton = findViewById(R.id.editSongMenu)
        deleteSongButton = findViewById(R.id.deleteSongMenu)
        addArtistButton?.setOnClickListener(View.OnClickListener { startActivity(Intent(this@MainActivity, AddArtistActivity::class.java)) })
        getArtistsButton?.setOnClickListener(View.OnClickListener { startActivity(Intent(this@MainActivity, ShowArtistsActivity::class.java)) })
        editArtistButton?.setOnClickListener(View.OnClickListener { startActivity(Intent(this@MainActivity, EditArtistActivity::class.java)) })
        deleteArtistButton?.setOnClickListener(View.OnClickListener { startActivity(Intent(this@MainActivity, DeleteArtistActivity::class.java)) })
        searchDeezerArtistButton?.setOnClickListener(View.OnClickListener { startActivity(Intent(this@MainActivity, DeezerArtistActivity::class.java)) })
        addSongButton?.setOnClickListener(View.OnClickListener { startActivity(Intent(this@MainActivity, AddSongActivity::class.java)) })
        getSongsButton?.setOnClickListener(View.OnClickListener { startActivity(Intent(this@MainActivity, ShowSongsActivity::class.java)) })
        editSongButton?.setOnClickListener(View.OnClickListener { startActivity(Intent(this@MainActivity, EditSongActivity::class.java)) })
        deleteSongButton?.setOnClickListener(View.OnClickListener { startActivity(Intent(this@MainActivity, DeleteSongActivity::class.java)) })
    }
}