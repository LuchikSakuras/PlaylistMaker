package com.example.playlistmaker.ui.main

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.main.MainInteractor

class MainViewModel(
    private val mainInteractor: MainInteractor,
): ViewModel() {

    fun updateTheme(){
        mainInteractor.updateTheme()
    }

}