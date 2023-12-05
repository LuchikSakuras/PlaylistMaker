package com.example.playlistmaker

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class TrackResult(
    val results: List<Track>,
)

@Parcelize
data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
) : Parcelable

