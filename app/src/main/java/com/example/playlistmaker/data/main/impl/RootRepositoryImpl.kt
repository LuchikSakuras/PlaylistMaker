package com.example.playlistmaker.data.main.impl

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.App
import com.example.playlistmaker.DARK_THEME_KEY
import com.example.playlistmaker.domain.main.repository.RootRepository

class RootRepositoryImpl(private val context: Context, private val sharedPrefs: SharedPreferences) :
    RootRepository {

    override fun updateTheme() {
        if (readIsThemeDark()) {
            (context.applicationContext as App).switchTheme(true)
        }
    }

    private fun readIsThemeDark(): Boolean {
        return sharedPrefs.getBoolean(DARK_THEME_KEY, false)
    }
}