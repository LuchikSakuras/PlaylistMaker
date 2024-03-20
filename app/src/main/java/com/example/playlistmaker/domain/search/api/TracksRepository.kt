package com.example.playlistmaker.domain.search.api
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import java.util.ArrayList

interface TracksRepository {
    fun search(expression: String): Flow<Resource<ArrayList<Track>>>

    fun addToHistory(track: Track)

    fun writeStoryList(storyList: ArrayList<Track>)

    fun readStoryList(): ArrayList<Track>

}