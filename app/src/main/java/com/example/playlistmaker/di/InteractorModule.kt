package com.example.playlistmaker.di

import com.example.playlistmaker.domain.main.interactor.RootInteractor
import com.example.playlistmaker.domain.main.impl.RootInteractorImpl
import com.example.playlistmaker.domain.player.interactor.PlayerInteractor
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.api.TracksInteractor
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.setting.SettingsInteractor
import com.example.playlistmaker.domain.setting.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val interactorModule = module {
    single<RootInteractor> {
        RootInteractorImpl(get())
    }
    single<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
    single<SharingInteractor> {
        SharingInteractorImpl(get(), androidContext())
    }
    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }
}


