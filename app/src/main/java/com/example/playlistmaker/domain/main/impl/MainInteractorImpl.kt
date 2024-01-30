package com.example.playlistmaker.domain.main.impl

import com.example.playlistmaker.domain.main.repository.MainRepository
import com.example.playlistmaker.domain.main.interactor.MainInteractor

class MainInteractorImpl(private val mainRepository: MainRepository): MainInteractor {
    override fun updateTheme(){
        mainRepository.updateTheme()
    }

}