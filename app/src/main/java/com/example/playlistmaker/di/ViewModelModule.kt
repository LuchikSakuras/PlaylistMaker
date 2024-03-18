package com.example.playlistmaker.di

import com.example.playlistmaker.ui.audioplayer.AudioPlayerViewModel
import com.example.playlistmaker.ui.medialibrary.viewModel.FavouritesFragmentViewModel
import com.example.playlistmaker.ui.medialibrary.viewModel.MediaLibraryViewModel
import com.example.playlistmaker.ui.medialibrary.viewModel.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.root.RootViewModel
import com.example.playlistmaker.ui.search.view_model.TracksSearchViewModel
import com.example.playlistmaker.ui.setting.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AudioPlayerViewModel(get())
    }
    viewModel {
        RootViewModel(get())
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