package com.example.playlistmaker

import com.google.gson.annotations.SerializedName

class TrackResult(
    val resultCount: Int,
    val results: List<Track>,
)

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
)


