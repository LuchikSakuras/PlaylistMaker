package com.example.playlistmaker.domain.main.impl

import com.example.playlistmaker.data.main.MainRepository
import com.example.playlistmaker.domain.main.MainInteractor

class MainInteractorImpl(private val mainRepository: MainRepository): MainInteractor {
    override fun updateTheme(){
        mainRepository.updateTheme()
    }

    override fun openSearch() {
        mainRepository.openSearch()
    }

    override fun openMediaLibrary() {
        mainRepository.openMediaLibrary()
    }

    override fun openSettings() {
        mainRepository.openSettings()
    }

}