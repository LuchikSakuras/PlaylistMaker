package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.TracksInteractor
import com.example.playlistmaker.domain.search.api.TracksRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.Resource
import java.util.ArrayList
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun search(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when(val resource = repository.search(expression)){
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.error) }
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
