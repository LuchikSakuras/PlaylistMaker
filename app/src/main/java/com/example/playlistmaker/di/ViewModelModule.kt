package com.example.playlistmaker.di

import com.example.playlistmaker.ui.audioplayer.AudioPlayerViewModel
import com.example.playlistmaker.ui.main.MainViewModel
import com.example.playlistmaker.ui.medialibrary.viewModel.FavouritesFragmentViewModel
import com.example.playlistmaker.ui.medialibrary.viewModel.MediaLibraryViewModel
import com.example.playlistmaker.ui.medialibrary.viewModel.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.search.TracksSearchViewModel
import com.example.playlistmaker.ui.setting.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AudioPlayerViewModel(get())
    }
    viewModel {
        MainViewModel(get())
    }
    viewModel {
        TracksSearchViewModel(get())
    }
    viewModel {
        SettingsViewModel(get(),get())
    }
    viewModel{
        MediaLibraryViewModel()
    }
    viewModel{
        FavouritesFragmentViewModel()
    }
    viewModel{
        PlaylistsFragmentViewModel()
    }
}