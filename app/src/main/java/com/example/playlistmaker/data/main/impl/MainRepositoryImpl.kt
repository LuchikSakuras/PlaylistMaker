package com.example.playlistmaker.data.main.impl

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.App
import com.example.playlistmaker.DARK_THEME_KEY
import com.example.playlistmaker.domain.main.repository.MainRepository

class MainRepositoryImpl(private val context: Context, private val sharedPrefs: SharedPreferences) :
    MainRepository {

    // private val sharedPrefs = context.getSharedPreferences(PREFERENCES, AppCompatActivity.MODE_PRIVATE)

    override fun updateTheme() {
        if (readIsThemeDark()) {
            (context.applicationContext as App).switchTheme(true)
        }
    }

    private fun readIsThemeDark(): Boolean {
        return sharedPrefs.getBoolean(DARK_THEME_KEY, false)
    }
}