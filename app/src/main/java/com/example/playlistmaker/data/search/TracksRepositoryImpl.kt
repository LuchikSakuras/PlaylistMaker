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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.ArrayList

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) :
    TracksRepository {

    private var storyList = readStoryList()
    override fun search(expression: String): Flow<Resource<ArrayList<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(-1))
            }

            200 -> {
                with(response as TracksSearchResponse) {
                    val data = results.map {
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
                    } as ArrayList<Track>
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error(-2))
            }
        }
    }

    override fun addToHistory(track: Track) {
        val trackDto = mapTrackToTrackDto(track)
        if (!storyList.contains(track)) {
            if (storyList.size == 10) {
                storyList.removeLast()
                addStoryList(trackDto)
            } else {
                addStoryList(trackDto)
            }
        } else {
            storyList.remove(track)
            addStoryList(trackDto)
        }
    }

    override fun writeStoryList(storyList: ArrayList<Track>) {
        val json = Gson().toJson(storyList)
        sharedPrefs.edit().putString(TRACK_LIST_KEY, json).apply()
    }

    override fun readStoryList(): ArrayList<Track> {
        val json = sharedPrefs.getString(TRACK_LIST_KEY, null)
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson(json, type) ?: ArrayList()
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

}

