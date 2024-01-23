package com.example.playlistmaker.domain.player.repository

import com.example.playlistmaker.domain.player.models.PlayState

interface PlayerRepository {

    fun updateState(): PlayState

    fun preparePlayer (previewUrl: String)

    fun startPlayer()

    fun pausePlayer()

    fun callbackForPrepared(callback:()-> Unit)

    fun callbackForCompletion(callback:()-> Unit)

    fun updateCurrentPosition(): Int

    fun releaseMediaPlayer()

    fun replaceSize(artworkUrl100: String): String

    fun simpleDateFormat(trackTimeMillis: Int): String

}