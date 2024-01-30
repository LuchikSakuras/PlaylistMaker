package com.example.playlistmaker.ui.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.ui.setting.view_model.SettingsViewModel
import com.example.playlistmaker.util.Creator

class SettingViewModelFactory(context: Context) : ViewModelProvider.NewInstanceFactory() {

    private val sharingInteractor = Creator.provideSharingInteractor(context)
    private val settingsInteractor = Creator.provideSettingsgInteractor(context)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(sharingInteractor, settingsInteractor) as T
    }
}