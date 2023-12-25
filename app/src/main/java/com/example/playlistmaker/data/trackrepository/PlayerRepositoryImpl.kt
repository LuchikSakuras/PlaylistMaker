package com.example.playlistmaker.data.trackrepository

import android.annotation.SuppressLint
import android.media.MediaPlayer
import com.example.playlistmaker.data.model.PlayStateDto
import com.example.playlistmaker.data.model.TrackDto
import com.example.playlistmaker.domain.models.PlayState
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.PlayerRepository

class PlayerRepositoryImpl : PlayerRepository {

    private val mediaPlayer = MediaPlayer()
    private var playerStateDto = PlayStateDto.STATE_DEFAULT
    private var callbackForCompletion: (()-> Unit)? = null
    private var callbackForPrepared: (()-> Unit)? = null

    @SuppressLint("SuspiciousIndentation")
    override fun preparePlayer (previewUrl: String) {

            mediaPlayer.setDataSource(previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerStateDto = PlayStateDto.STATE_PREPARED
                callbackForPrepared?.invoke()
            }
            mediaPlayer.setOnCompletionListener {
                playerStateDto = PlayStateDto.STATE_PREPARED
                callbackForCompletion?.invoke()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerStateDto = PlayStateDto.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerStateDto = PlayStateDto.STATE_PAUSED
    }

    override fun callbackForPrepared(callback:()-> Unit){
        this.callbackForPrepared = callback
    }

    override fun callbackForCompletion(callback:()-> Unit){
        this.callbackForCompletion = callback
    }

    override fun updateCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun releaseMediaPlayer(){
        mediaPlayer.release()
        callbackForCompletion = null
        callbackForPrepared = null
    }

    override fun updateState(): PlayState {
        return when (playerStateDto){
            PlayStateDto.STATE_DEFAULT -> PlayState.STATE_DEFAULT
            PlayStateDto.STATE_PREPARED -> PlayState.STATE_PREPARED
            PlayStateDto.STATE_PLAYING-> PlayState.STATE_PLAYING
            PlayStateDto.STATE_PAUSED -> PlayState.STATE_PAUSED
        }
    }


}