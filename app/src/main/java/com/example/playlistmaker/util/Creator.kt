package com.example.playlistmaker.util

import android.app.Application
import android.content.Context
import com.example.playlistmaker.data.main.MainRepository
import com.example.playlistmaker.data.main.impl.MainRepositoryImpl
import com.example.playlistmaker.data.player.trackrepository.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.TracksRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.setting.SettingsRepository
import com.example.playlistmaker.data.setting.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.main.MainInteractor
import com.example.playlistmaker.domain.main.impl.MainInteractorImpl
import com.example.playlistmaker.domain.player.Interactor.PlayerInteractor
import com.example.playlistmaker.domain.player.repository.PlayerRepository
import com.example.playlistmaker.ui.search.TracksSearchViewModel
import com.example.playlistmaker.domain.search.api.TracksInteractor
import com.example.playlistmaker.domain.search.api.TracksRepository
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.setting.SettingsInteractor
import com.example.playlistmaker.domain.setting.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractor(providePlayerRepository())
    }

    private fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context), context)
    }
    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context), context)
    }

    private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun provideSettingsgInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    fun provideMaingInteractor(context: Context): MainInteractor {
        return MainInteractorImpl(getMainRepository(context))
    }

    private fun getMainRepository(context: Context): MainRepository {
        return MainRepositoryImpl(context)
    }

}