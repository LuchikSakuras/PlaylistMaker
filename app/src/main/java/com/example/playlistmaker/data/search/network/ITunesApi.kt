package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.model.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search")
    fun search(@Query("term") term: String): Call<TracksSearchResponse>
}