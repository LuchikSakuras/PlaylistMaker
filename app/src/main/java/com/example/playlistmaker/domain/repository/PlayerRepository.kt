package com.example.playlistmaker.domain.repository


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

}