package com.example.playlistmaker.domain.main.impl

import com.example.playlistmaker.domain.main.MainRepository
import com.example.playlistmaker.domain.main.MainInteractor

class MainInteractorImpl(private val mainRepository: MainRepository): MainInteractor {
    override fun updateTheme(){
        mainRepository.updateTheme()
    }

}