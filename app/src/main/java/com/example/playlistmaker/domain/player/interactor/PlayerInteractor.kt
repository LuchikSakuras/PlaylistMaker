package com.example.playlistmaker.domain.player.interactor

import com.example.playlistmaker.domain.player.models.PlayState

interface PlayerInteractor {
    fun updateState(): PlayState
    fun preparePlayer(previewUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun callbackForPrepared(callback: () -> Unit)
    fun callbackForCompletion(callback: () -> Unit)
    fun updateCurrentPosition(): Int
    fun releaseMediaPlayer()
    fun replaceSize(artworkUrl100: String): String
    fun simpleDateFormat(trackTimeMillis: Int): String
}