package com.example.playlistmaker.ui.audioplayer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.TRACK_KEY
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.player.models.PlayState
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

import java.util.*

@Suppress("DEPRECATION")
class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private var playerState: PlayState = PlayState.STATE_DEFAULT

    private val viewModel by viewModel<AudioPlayerViewModel>()
    private var timerJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModel.stateLiveData.observe(this, androidx.lifecycle.Observer {
            playerState = it
        })
        viewModel.positionLiveData.observe(this, androidx.lifecycle.Observer {
            binding.timePlay.text = it
        })
        viewModel.simpleDateFormatLiveData.observe(this, androidx.lifecycle.Observer {
            binding.trackTimeMillsTrack.text = it
        })


        val track = intent.getParcelableExtra<Track>(TRACK_KEY)

        if (track != null) {
            preparePlayer(track)
            binding.trackName.text = track.trackName
            binding.artistName.text = track.artistName
            binding.releaseDateTrack.text = track.releaseDate.substring(0, 4)
            binding.primaryGenreNameTrack.text = track.primaryGenreName
            binding.countryTrack.text = track.country

            Glide.with(binding.imageTrack.context).load(viewModel.replaceSize(track.artworkUrl100))
                .placeholder(R.drawable.placeholder).transform(
                    FitCenter(),
                    RoundedCorners(
                        binding.imageTrack.resources.getDimensionPixelSize(R.dimen.dp_8)
                    ),
                ).into(binding.imageTrack)

            viewModel.simpleDateFormat(track.trackTimeMillis)
            if (track.collectionName!!.isEmpty()) {
                binding.collectionNameTrack.isVisible = false
                binding.collectionName.isVisible = false
            } else {
                binding.collectionNameTrack.isVisible = true
                binding.collectionName.isVisible = true
                binding.collectionNameTrack.text = track.collectionName
            }
        }

        binding.buttonPlay.setOnClickListener {
            playbackControl()
        }

        binding.arrowBack.setOnClickListener {
            finish()
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releaseMediaPlayer()
        timerJob = null
    }

    companion object {
        private const val TIMER_DELAY = 300L
        private const val ZERO_VALUE = "00:00"
    }


    private fun preparePlayer(track: Track) {
        viewModel.preparePlayer(track.previewUrl)
        viewModel.callbackForCompletion {
            binding.timePlay.text = ZERO_VALUE
        }

        viewModel.callbackForPrepared {
            binding.buttonPlay.isEnabled = true
            playerState = PlayState.STATE_PREPARED
        }
    }

    private fun playbackControl() {
        viewModel.updateState()
        when (playerState) {
            PlayState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayState.STATE_PREPARED, PlayState.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {
                playerState
            }
        }
    }

    private fun startPlayer() {
        binding.buttonPlay.setBackgroundResource(R.drawable.button_pause)
        viewModel.startPlayer()
         startTimer()
    }

    private fun pausePlayer() {
        binding.buttonPlay.setBackgroundResource(R.drawable.button_play)
        timerJob?.cancel()
        viewModel.pausePlayer()
    }


   private fun startTimer() {
        viewModel.updateState()
        timerJob = lifecycleScope.launch {
            while (playerState == PlayState.STATE_PLAYING) {
                delay(TIMER_DELAY)
                viewModel.updatePosition()
                viewModel.updateState()
            }
            binding.buttonPlay.setBackgroundResource(R.drawable.button_play)
            binding.buttonPlay.isEnabled = true
        }
    }

}
