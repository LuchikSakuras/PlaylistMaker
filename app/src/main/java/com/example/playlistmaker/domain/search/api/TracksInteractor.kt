package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import java.util.ArrayList

interface TracksInteractor {
    fun search(expression: String): Flow<Pair<ArrayList<Track>?, Int?>>

    fun addToHistory(track: Track)

    fun writeStoryList(storyList: ArrayList<Track>)

    fun readStoryList(): ArrayList<Track>

}