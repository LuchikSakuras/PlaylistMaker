package com.example.playlistmaker.data.setting.impl


import android.content.SharedPreferences
import com.example.playlistmaker.DARK_THEME_KEY
import com.example.playlistmaker.domain.setting.SettingsRepository
import com.example.playlistmaker.domain.setting.model.ThemeSettings

class SettingsRepositoryImpl(
    private val sharedPrefs: SharedPreferences
) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return if (sharedPrefs.getBoolean(DARK_THEME_KEY, false)) {
            ThemeSettings(true)
        } else {
            ThemeSettings(false)
        }
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPrefs.edit().putBoolean(DARK_THEME_KEY, settings.night).apply()
    }
}