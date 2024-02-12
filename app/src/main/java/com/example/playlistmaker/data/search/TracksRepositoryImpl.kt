package com.example.playlistmaker.data.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.TRACK_KEY
import com.example.playlistmaker.TRACK_LIST_KEY
import com.example.playlistmaker.data.search.model.TrackDto
import com.example.playlistmaker.data.search.model.TracksSearchRequest
import com.example.playlistmaker.data.search.model.TracksSearchResponse
import com.example.playlistmaker.domain.search.api.TracksRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioplayer.AudioPlayerActivity
import com.example.playlistmaker.util.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val context: Context,
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) :
    TracksRepository {

    //private var sharedPrefs = context.getSharedPreferences(PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    private var storyList = readStoryList()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    override fun search(expression: String): Resource<ArrayList<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        when (response.resultCode) {
            -1 -> {
                return Resource.Error(-1)
            }

            200 -> {
                val tracks = ArrayList<Track>()
                (response as TracksSearchResponse).results.mapTo(tracks) {
                    Track(
                        it.trackName ?: "",
                        it.artistName ?: "",
                        it.trackTimeMillis ?: 0,
                        it.artworkUrl100 ?: "",
                        it.collectionName ?: "",
                        it.releaseDate ?: "",
                        it.primaryGenreName ?: "",
                        it.country ?: "",
                        it.previewUrl ?: ""
                    )
                }
                return Resource.Success(tracks)
            }

            else -> {
                return Resource.Error(-2)
            }
        }
    }

    override fun addToHistory(track: Track) {
        val trackDto = mapTrackToTrackDto(track)
        if (!storyList.contains(track)) {
            if (storyList.size == 10) {
                storyList.removeLast()
                goToPlayer(trackDto)
            } else {
                goToPlayer(trackDto)
            }
        } else {
            storyList.remove(track)
            goToPlayer(trackDto)
        }
    }

    override fun writeStoryList(storyList: ArrayList<Track>) {
        val json = Gson().toJson(storyList)
        sharedPrefs.edit().putString(TRACK_LIST_KEY, json).apply()
    }

    override fun readStoryList(): ArrayList<Track> {
        //  val gson = Gson()
        val json = sharedPrefs.getString(TRACK_LIST_KEY, null)
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson(json, type) ?: ArrayList()
    }

    private fun goToPlayer(trackDto: TrackDto) {
        addStoryList(trackDto)

        val track = mapTrackDtoToTrack(trackDto)

        if (clickDebounce()) {
            val intent = Intent(context, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, track)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun addStoryList(trackDto: TrackDto) {
        storyList.add(0, mapTrackDtoToTrack(trackDto))
        writeStoryList(storyList)
    }


    private fun mapTrackToTrackDto(track: Track): TrackDto {

        return TrackDto(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    private fun mapTrackDtoToTrack(trackDto: TrackDto): Track {

        return Track(
            trackDto.trackName,
            trackDto.artistName,
            trackDto.trackTimeMillis,
            trackDto.artworkUrl100,
            trackDto.collectionName,
            trackDto.releaseDate,
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.previewUrl
        )
    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }


}

