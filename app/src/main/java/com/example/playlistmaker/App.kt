package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val PREFERENCES = "preferences"
const val DARK_THEME_KEY = "key_for_theme"
const val TRACK_LIST_KEY = "key_for_track_list"
const val TRACK_KEY = "key_for_track"

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
    }


    //метод смены темы
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            //если свитч вкл, ночная тема вкл
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                //если свитч выкл, ночная тема выкл
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
