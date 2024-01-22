package com.example.playlistmaker.ui.search

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.util.Creator

class TrackSearchViewModelFactory(context: Context): ViewModelProvider.Factory {

    private val tracksInteractor = Creator.provideTracksInteractor(context)

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return TracksSearchViewModel(tracksInteractor) as T
    }
}