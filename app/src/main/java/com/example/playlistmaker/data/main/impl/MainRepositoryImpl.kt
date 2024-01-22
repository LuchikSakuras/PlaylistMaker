package com.example.playlistmaker.data.main.impl

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.DARK_THEME_KEY
import com.example.playlistmaker.PREFERENCES
import com.example.playlistmaker.data.main.MainRepository
import com.example.playlistmaker.ui.medialibrary.MediaLibraryActivity
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.setting.activity.SettingsActivity

class MainRepositoryImpl(private val context: Context): MainRepository {

    private lateinit var sharedPrefs: SharedPreferences

    override fun updateTheme(){
        sharedPrefs = context.getSharedPreferences(PREFERENCES, AppCompatActivity.MODE_PRIVATE)
        if (readSwitcher()) {
            (context.applicationContext as App).switchTheme(true)
        }
    }

    override fun openSearch(){
        val displayIntent = Intent(context, SearchActivity::class.java)
        context.startActivity(displayIntent)
    }

    override fun openMediaLibrary(){
        val displayIntent = Intent(context, MediaLibraryActivity::class.java)
        context.startActivity(displayIntent)
    }

    override fun openSettings(){
        val displayIntent = Intent(context, SettingsActivity::class.java)
        context.startActivity(displayIntent)
    }

    private fun readSwitcher(): Boolean {
        return sharedPrefs.getBoolean(DARK_THEME_KEY, false)
    }
}