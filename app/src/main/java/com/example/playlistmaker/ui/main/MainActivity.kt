package com.example.playlistmaker.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.App
import com.example.playlistmaker.ui.medialibrary.MediaLibraryActivity
import com.example.playlistmaker.PREFERENCES
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.setting.SettingsActivity
import com.example.playlistmaker.UserPreferences
import com.example.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefs: SharedPreferences
    private val userPreferences = UserPreferences()

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        if (userPreferences.readSwitcher(sharedPrefs)) {
            (applicationContext as App).switchTheme(true)
        }

        binding.buttonSearch.setOnClickListener {
            val displayIntent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        binding.buttonMediaLibrary.setOnClickListener {
            val displayIntent = Intent(this@MainActivity, MediaLibraryActivity::class.java)
            startActivity(displayIntent)
        }
        binding.buttonSettings.setOnClickListener {
            val displayIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(displayIntent)

        }
    }
}

