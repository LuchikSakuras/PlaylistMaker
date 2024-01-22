package com.example.playlistmaker.ui.audioplayer

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.models.PlayState
import com.example.playlistmaker.util.Creator
import java.util.Locale

class AudioPlayerViewModel : ViewModel() {

    private val playerInteractor = Creator.providePlayerInteractor()
    var stateLiveData = MutableLiveData<PlayState>()
    var positionLiveData = MutableLiveData<String>()
    var simpleDateFormatLiveData = MutableLiveData<String>()


    fun simpleDateFormat(trackTimeMillis: Int){
        simpleDateFormatLiveData.value = playerInteractor.simpleDateFormat(trackTimeMillis)
    }

    fun updateState(){
        stateLiveData.value = playerInteractor.updateState()
    }

    fun updatePosition(){
        positionLiveData.value = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerInteractor.updateCurrentPosition())
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
    }

    fun releaseMediaPlayer(){
        playerInteractor.releaseMediaPlayer()
    }

    fun preparePlayer(previewUrl: String){
        playerInteractor.preparePlayer(previewUrl)
    }


    fun callbackForCompletion(callback: () -> Unit) {
        playerInteractor.callbackForCompletion(callback)
    }

    fun callbackForPrepared(callback: () -> Unit) {
        playerInteractor.callbackForPrepared(callback)
    }

    fun startPlayer(){
        playerInteractor.startPlayer()
    }

    fun replaceSize(artworkUrl100: String): String{
        return playerInteractor.replaceSize(artworkUrl100)
    }



}