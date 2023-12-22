package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.model.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search")
    fun search(@Query("term") term: String): Call<TrackResponse>
}