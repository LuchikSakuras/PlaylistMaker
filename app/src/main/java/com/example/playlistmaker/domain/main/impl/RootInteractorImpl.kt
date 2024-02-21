package com.example.playlistmaker.domain.main.impl

import com.example.playlistmaker.domain.main.repository.RootRepository
import com.example.playlistmaker.domain.main.interactor.RootInteractor

class RootInteractorImpl(private val rootRepository: RootRepository): RootInteractor {
    override fun updateTheme(){
        rootRepository.updateTheme()
    }

}