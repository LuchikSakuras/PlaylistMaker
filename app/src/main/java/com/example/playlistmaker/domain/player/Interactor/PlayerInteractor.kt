package com.example.playlistmaker.domain.player.Interactor

import com.example.playlistmaker.domain.player.models.PlayState
import com.example.playlistmaker.domain.player.repository.PlayerRepository

class PlayerInteractor(private val playerRepository: PlayerRepository) {

    fun updateState(): PlayState {
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
    fun replaceSize(artworkUrl100: String): String{
        return playerRepository.replaceSize(artworkUrl100)
    }

    fun simpleDateFormat(trackTimeMillis: Int): String{
        return playerRepository.simpleDateFormat(trackTimeMillis)
    }

}