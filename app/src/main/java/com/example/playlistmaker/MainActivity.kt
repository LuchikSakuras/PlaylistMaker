package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch: Button = findViewById<Button>(R.id.button_search)
        val buttonMediaLibrary: Button = findViewById<Button>(R.id.button_media_library)
        val buttonSettings: Button = findViewById<Button>(R.id.button_settings)


        buttonSearch.setOnClickListener {
            val displayIntent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        buttonMediaLibrary.setOnClickListener {
            val displayIntent = Intent(this@MainActivity, MediaLibraryActivity::class.java)
            startActivity(displayIntent)
        }

        buttonSettings.setOnClickListener {
            val displayIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}

