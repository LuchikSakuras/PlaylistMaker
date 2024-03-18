package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.model.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
