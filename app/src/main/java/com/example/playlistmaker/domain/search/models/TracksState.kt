package com.example.playlistmaker.domain.search.models


import java.util.ArrayList

sealed  interface TracksState {
    object Loading : TracksState
    data class Content(
        val tracks: ArrayList<Track>
    ) : TracksState
    object Error : TracksState
    object Empty : TracksState

}