package com.example.playlistmaker

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
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding

import java.util.*

@Suppress("DEPRECATION")
class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preparePlayer()
        handler = Handler(Looper.getMainLooper())

        val track = intent.getParcelableExtra<Track>(TRACK_KEY)
        if (track != null) {
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
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val HALF_SECOND = 500L
    }

    @SuppressLint("SetTextI18n")
    private fun preparePlayer() {
        mediaPlayer.setDataSource(intent.getParcelableExtra<Track>(TRACK_KEY)?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.buttonPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            binding.timePlay.text = "00:00"
        }
    }
    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.buttonPlay.setBackgroundResource(R.drawable.button_pause)
        playerState = STATE_PLAYING
        updateCurrentPosition()
    }


    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.buttonPlay.setBackgroundResource(R.drawable.button_play)
        playerState = STATE_PAUSED
    }

    private fun updateCurrentPosition() {
        val myThread =
            object : Runnable {
                override fun run() {
                    if (playerState == STATE_PLAYING) {
                        binding.timePlay.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
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