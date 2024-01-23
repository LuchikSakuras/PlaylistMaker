package com.example.playlistmaker.ui.setting.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.setting.SettingsInteractor
import com.example.playlistmaker.domain.setting.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    private var checkMutableLiveData = MutableLiveData<Boolean>()
    val checkLiveData: LiveData<Boolean>
        get() = checkMutableLiveData


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
        checkMutableLiveData.value = settings.night
    }

    fun updateThemeSetting(themeSettings: ThemeSettings) {
        settingsInteractor.updateThemeSetting(themeSettings)
    }


}
