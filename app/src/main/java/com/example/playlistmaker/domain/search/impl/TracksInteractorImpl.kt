package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.TracksInteractor
import com.example.playlistmaker.domain.search.api.TracksRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.ArrayList
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    override fun search(expression: String): Flow<Pair<ArrayList<Track>?, Int?>> {

        return repository.search(expression).map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)

                is Resource.Error -> Pair(null, result.error)
            }
        }
    }

    override fun addToHistory(track: Track) {
        repository.addToHistory(track)
    }

    override fun writeStoryList(storyList: ArrayList<Track>) {
        repository.writeStoryList(storyList)
    }

    override fun readStoryList(): ArrayList<Track> {
        return repository.readStoryList()
    }

}
