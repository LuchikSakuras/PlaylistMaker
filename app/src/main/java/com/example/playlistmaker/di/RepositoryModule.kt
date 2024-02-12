package com.example.playlistmaker.di

import com.example.playlistmaker.data.main.impl.MainRepositoryImpl
import com.example.playlistmaker.data.player.trackrepository.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.TracksRepositoryImpl
import com.example.playlistmaker.data.setting.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.main.repository.MainRepository
import com.example.playlistmaker.domain.player.repository.PlayerRepository
import com.example.playlistmaker.domain.search.api.TracksRepository
import com.example.playlistmaker.domain.setting.SettingsRepository
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    single<MainRepository> {
        MainRepositoryImpl(androidContext(), get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get(), androidContext(), get(),  get())
    }

    single<PlayerRepository> {
        PlayerRepositoryImpl()
    }

}

