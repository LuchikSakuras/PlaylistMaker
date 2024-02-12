package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.interactor.PlayerInteractor
import com.example.playlistmaker.domain.player.models.PlayState
import com.example.playlistmaker.domain.player.repository.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {

    override fun updateState(): PlayState {
        return playerRepository.updateState()
    }

    override fun preparePlayer(previewUrl: String) {
        playerRepository.preparePlayer(previewUrl)
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun callbackForPrepared(callback: () -> Unit) {
        playerRepository.callbackForPrepared(callback)
    }

    override fun callbackForCompletion(callback: () -> Unit) {
        playerRepository.callbackForCompletion(callback)
    }

    override fun updateCurrentPosition(): Int {
        return playerRepository.updateCurrentPosition()
    }

    override fun releaseMediaPlayer() {
        playerRepository.releaseMediaPlayer()
    }

    override fun replaceSize(artworkUrl100: String): String {
        return playerRepository.replaceSize(artworkUrl100)
    }

    override fun simpleDateFormat(trackTimeMillis: Int): String {
        return playerRepository.simpleDateFormat(trackTimeMillis)
    }

}