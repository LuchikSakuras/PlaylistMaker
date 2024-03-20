package com.example.playlistmaker.ui.setting.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.setting.SettingsInteractor
import com.example.playlistmaker.domain.setting.model.ThemeSettings

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    private var checkMutableLiveData = MutableLiveData<Boolean>()
    val checkLiveData: LiveData<Boolean>
        get() = checkMutableLiveData

    fun themeSwitcherIsChecked() {
        val settings: ThemeSettings = settingsInteractor.getThemeSettings()
        checkMutableLiveData.value = settings.night
    }

    fun updateThemeSetting(themeSettings: ThemeSettings) {
        settingsInteractor.updateThemeSetting(themeSettings)
    }


}
