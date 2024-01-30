package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track
import java.util.ArrayList

interface TracksInteractor {
    fun search(expression: String, consumer: TracksConsumer)
    interface TracksConsumer {
        fun consume(tracks: ArrayList<Track>?, error: Int?)
    }

    fun addToHistory(track: Track)

    fun writeStoryList(storyList: ArrayList<Track>)

    fun readStoryList(): ArrayList<Track>

}