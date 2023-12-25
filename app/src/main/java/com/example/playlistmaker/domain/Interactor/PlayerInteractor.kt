package com.example.playlistmaker.domain.Interactor

import com.example.playlistmaker.domain.models.PlayState
import com.example.playlistmaker.domain.repository.PlayerRepository

class PlayerInteractor(private val playerRepository: PlayerRepository) {

    fun updateState(): PlayState{
        return playerRepository.updateState()
    }

    fun preparePlayer(previewUrl: String){
        playerRepository.preparePlayer(previewUrl)
    }

    fun startPlayer(){
        playerRepository.startPlayer()
    }

    fun pausePlayer(){
        playerRepository.pausePlayer()
    }

    fun callbackForPrepared(callback: ()-> Unit){
        playerRepository.callbackForPrepared(callback)
    }

    fun callbackForCompletion(callback: ()-> Unit){
        playerRepository.callbackForCompletion(callback)
    }

    fun updateCurrentPosition(): Int{
        return playerRepository.updateCurrentPosition()
    }

    fun releaseMediaPlayer(){
        playerRepository.releaseMediaPlayer()
    }

}