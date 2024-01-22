package com.example.playlistmaker.ui.setting.view_model

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.models.PlayState
import com.example.playlistmaker.domain.search.models.TracksState
import com.example.playlistmaker.domain.setting.SettingsInteractor
import com.example.playlistmaker.domain.setting.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    var checkLiveData = MutableLiveData<Boolean>()

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openTerms()
    }

    fun themeSwitcherIsChecked() {
        val settings: ThemeSettings = settingsInteractor.getThemeSettings()
        checkLiveData.value = settings.night
    }

    fun updateThemeSetting(themeSettings: ThemeSettings) {
        settingsInteractor.updateThemeSetting(themeSettings)
    }


}
