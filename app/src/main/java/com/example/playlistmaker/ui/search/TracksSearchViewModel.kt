package com.example.playlistmaker.ui.search

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.search.api.TracksInteractor
import java.util.ArrayList
import com.example.playlistmaker.data.search.model.TrackDto
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.models.TracksState

class TracksSearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {

    private var latestSearchText: String? = null

    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<TracksState>()

     val storyListLiveData = MutableLiveData<ArrayList<Track>>()

    fun observeState(): LiveData<TracksState> = stateLiveData
    fun chekStoryList() {
        storyListLiveData.value = readStoryList()
    }
    public override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { searchTextRequest(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun searchTextRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)
            tracksInteractor.search(newSearchText, object : TracksInteractor.TracksConsumer {
                override fun consume(tracks: ArrayList<Track>?, error: Int?) {
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

            })
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    fun addToHistory(track: Track){
        tracksInteractor.addToHistory(track)
    }


    fun writeStoryList(storyList: ArrayList<Track>) {
        tracksInteractor.writeStoryList(storyList)
    }

    fun readStoryList(): ArrayList<Track> {
        return tracksInteractor.readStoryList()
    }




 /*   fun addToHistory(track: Track, storyList: ArrayList<Track>){
        tracksInteractor.addToHistory(track, storyList)
    }*/


}

