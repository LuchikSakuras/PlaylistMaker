package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    lateinit var sharedPrefs: SharedPreferences
    val userPreferences = UserPreferences()

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch: Button = findViewById(R.id.button_search)
        val buttonMediaLibrary: Button = findViewById(R.id.button_media_library)
        val buttonSettings: Button = findViewById(R.id.button_settings)

        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)

        if(userPreferences.readSwitcher(sharedPrefs)){
            (applicationContext as App).switchTheme(true)
        }


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

