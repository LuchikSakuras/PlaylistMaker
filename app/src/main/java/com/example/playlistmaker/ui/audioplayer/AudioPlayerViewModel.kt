package com.example.playlistmaker.ui.audioplayer

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.interactor.PlayerInteractor
import com.example.playlistmaker.domain.player.models.PlayState
import java.util.Locale

class AudioPlayerViewModel( private val playerInteractor: PlayerInteractor) : ViewModel() {

    private var stateMutableLiveData = MutableLiveData<PlayState>()
    val stateLiveData: LiveData<PlayState>
        get() = stateMutableLiveData

    private var positionMutableLiveData = MutableLiveData<String>()
    val positionLiveData: LiveData<String>
        get() = positionMutableLiveData

    private var simpleDateFormatMutableLiveData = MutableLiveData<String>()
    val simpleDateFormatLiveData: LiveData<String>
        get() = simpleDateFormatMutableLiveData

    fun simpleDateFormat(trackTimeMillis: Int){
        simpleDateFormatMutableLiveData.value = playerInteractor.simpleDateFormat(trackTimeMillis)
    }

    fun updateState(){
        stateMutableLiveData.value = playerInteractor.updateState()
    }

    fun updatePosition(){
        positionMutableLiveData.value = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerInteractor.updateCurrentPosition())
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