package com.example.playlistmaker.di

import com.example.playlistmaker.data.main.impl.RootRepositoryImpl
import com.example.playlistmaker.data.player.trackrepository.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.TracksRepositoryImpl
import com.example.playlistmaker.data.setting.impl.SettingsRepositoryImpl
import com.example.playlistmaker.domain.main.repository.RootRepository
import com.example.playlistmaker.domain.player.repository.PlayerRepository
import com.example.playlistmaker.domain.search.api.TracksRepository
import com.example.playlistmaker.domain.setting.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<RootRepository> {
        RootRepositoryImpl(androidContext(), get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get(),  get())
    }

    single<PlayerRepository> {
        PlayerRepositoryImpl()
    }

}

