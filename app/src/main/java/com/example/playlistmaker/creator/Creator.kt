package com.example.playlistmaker.creator

import com.example.playlistmaker.data.trackrepository.PlayerRepositoryImpl
import com.example.playlistmaker.domain.Interactor.PlayerInteractor
import com.example.playlistmaker.domain.repository.PlayerRepository

object Creator {

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractor(providePlayerRepository())
    }

    private fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

}