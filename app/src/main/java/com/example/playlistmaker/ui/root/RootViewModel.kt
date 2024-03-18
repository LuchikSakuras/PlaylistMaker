package com.example.playlistmaker.ui.root

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.main.interactor.RootInteractor

class RootViewModel(private val playerInteractor: RootInteractor): ViewModel() {

    fun updateTheme(){
        playerInteractor.updateTheme()
    }

}