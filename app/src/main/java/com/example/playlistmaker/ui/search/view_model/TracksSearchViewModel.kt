package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.api.TracksInteractor
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.models.TracksState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class TracksSearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {

    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    private var stateMutableLiveData = MutableLiveData<TracksState>()

    private var storyMutableListLiveData = MutableLiveData<ArrayList<Track>>()
    val storyListLiveData: LiveData<ArrayList<Track>>
        get() = storyMutableListLiveData

    fun observeState(): LiveData<TracksState> = stateMutableLiveData
    fun checkStoryList() {
        storyMutableListLiveData.value = readStoryList()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchTextRequest(changedText)
        }
    }

    fun searchTextRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)
            viewModelScope.launch {
                tracksInteractor
                    .search(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }


    private fun processResult(tracks: ArrayList<Track>?, error: Int?) {
        val trackList = ArrayList<Track>()
        if (tracks != null && tracks.size != 0) {
            trackList.clear()
            trackList.addAll(tracks)
            renderState(TracksState.Content(trackList))
        } else if (error == -1) {
            renderState(TracksState.Error)
        } else {
            renderState(TracksState.Empty)
        }
    }

    private fun renderState(state: TracksState) {
        stateMutableLiveData.postValue(state)
    }

    fun addToHistory(track: Track) {
        tracksInteractor.addToHistory(track)
    }

    fun writeStoryList(storyList: ArrayList<Track>) {
        tracksInteractor.writeStoryList(storyList)
    }

    private fun readStoryList(): ArrayList<Track> {
        return tracksInteractor.readStoryList()
    }


}

