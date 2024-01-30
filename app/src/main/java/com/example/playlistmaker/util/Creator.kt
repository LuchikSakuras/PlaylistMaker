/*
package com.example.playlistmaker.util

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.domain.main.repository.MainRepository
import com.example.playlistmaker.data.main.impl.MainRepositoryImpl
import com.example.playlistmaker.data.player.trackrepository.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.TracksRepositoryImpl
import com.example.playlistmaker.data.search.network.ITunesApi
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.setting.SettingsRepository
import com.example.playlistmaker.data.setting.impl.SettingsRepositoryImpl
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.main.interactor.MainInteractor
import com.example.playlistmaker.domain.main.impl.MainInteractorImpl
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.player.repository.PlayerRepository
import com.example.playlistmaker.domain.search.api.TracksInteractor
import com.example.playlistmaker.domain.search.api.TracksRepository
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.setting.SettingsInteractor
import com.example.playlistmaker.domain.setting.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.google.gson.Gson

object Creator {

    fun providePlayerInteractor(): PlayerInteractorImpl {
        return PlayerInteractorImpl(providePlayerRepository())
    }

    private fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    private fun getTracksRepository(context: Context, sharedPrefs: SharedPreferences, gson: Gson, api: ITunesApi): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context, api), context, sharedPrefs, gson)
    }
    fun provideTracksInteractor(context: Context, sharedPrefs: SharedPreferences, gson: Gson, api: ITunesApi): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context, sharedPrefs, gson, api))
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

}*/
