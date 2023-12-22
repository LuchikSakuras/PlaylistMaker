package com.example.playlistmaker.ui.audioplayer

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.TRACK_KEY
import com.example.playlistmaker.data.trackrepository.PlayerRepositoryImpl
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.PlayState
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.PlayerRepository
import com.example.playlistmaker.domain.usecase.PlayerUseCase

import java.util.*

@Suppress("DEPRECATION")
class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private var playerState = PlayState.STATE_DEFAULT
    private lateinit var handler: Handler

    private val playerRepository: PlayerRepository = PlayerRepositoryImpl()
    private val playerUseCase = PlayerUseCase(playerRepository)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler(Looper.getMainLooper())

        val track = intent.getParcelableExtra<Track>(TRACK_KEY)

        if (track != null) {
            preparePlayer()
            binding.trackName.text = track.trackName
            binding.artistName.text = track.artistName
            binding.releaseDateTrack.text = track.releaseDate.substring(0, 4)
            binding.primaryGenreNameTrack.text = track.primaryGenreName
            binding.countryTrack.text = track.country

            Glide.with(binding.imageTrack.context).load(replaceSize(track))
                .placeholder(R.drawable.placeholder).transform(
                    FitCenter(),
                    RoundedCorners(
                        binding.imageTrack.resources.getDimensionPixelSize(R.dimen.dp_8)
                    ),
                ).into(binding.imageTrack)

            binding.trackTimeMillsTrack.text = SimpleDateFormat(
                "mm:ss", Locale.getDefault()
            ).format(track.trackTimeMillis.toLong())

            if (track.collectionName.isEmpty()) {
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
        playerUseCase.pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        playerUseCase.releaseMediaPlayer()
    }

  companion object {
        private const val HALF_SECOND = 500L
    }

    @SuppressLint("SetTextI18n")
    private fun preparePlayer() {
        val track = intent.getParcelableExtra<Track>(TRACK_KEY)
        if (track != null) {
            playerUseCase.preparePlayer(track)
        }
        playerUseCase.callbackForCompletion {
            binding.timePlay.text = "00:00"
        }

        playerUseCase.callbackForPrepared {
            binding.buttonPlay.isEnabled = true
        }
    }

    private fun playbackControl() {
        playerState = playerUseCase.updateState()
       when(playerState) {
           PlayState.STATE_PLAYING -> {
               playerUseCase.pausePlayer()
               pausePlayer()
            }
           PlayState.STATE_PREPARED, PlayState.STATE_PAUSED -> {
               playerUseCase.startPlayer()
               startPlayer()
            }

           else -> {playerState}
       }
    }

  private fun startPlayer() {
        binding.buttonPlay.setBackgroundResource(R.drawable.button_pause)
        updateCurrentPosition()
    }

    private fun pausePlayer() {
        binding.buttonPlay.setBackgroundResource(R.drawable.button_play)
    }

    private fun updateCurrentPosition() {
        val myThread =
            object : Runnable {
                override fun run() {
                    if (playerUseCase.updateState() == PlayState.STATE_PLAYING)
                    {
                        val currentPositionInMillis = playerUseCase.updateCurrentPosition()
                        binding.timePlay.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPositionInMillis)
                        handler.postDelayed(this, HALF_SECOND)

                    } else {
                        binding.buttonPlay.setBackgroundResource(R.drawable.button_play)
                        binding.buttonPlay.isEnabled = true
                    }
                }
            }
        handler.post(myThread)
    }
}

fun replaceSize(track: Track): String {
    return track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}