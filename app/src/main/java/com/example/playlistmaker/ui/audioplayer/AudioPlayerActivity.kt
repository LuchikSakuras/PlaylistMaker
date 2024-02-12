package com.example.playlistmaker.ui.audioplayer

import android.annotation.SuppressLint
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
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.player.models.PlayState
import com.example.playlistmaker.domain.search.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

import java.util.*

@Suppress("DEPRECATION")
class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private var playerState = PlayState.STATE_DEFAULT
    private lateinit var handler: Handler
    private val viewModel by viewModel<AudioPlayerViewModel>()

   // private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


       // viewModel = ViewModelProvider(this)[AudioPlayerViewModel::class.java]

        viewModel.stateLiveData.observe(this, androidx.lifecycle.Observer {
            playerState = it
        })
        viewModel.positionLiveData.observe(this, androidx.lifecycle.Observer {
            binding.timePlay.text = it
        })
        viewModel.simpleDateFormatLiveData.observe(this, androidx.lifecycle.Observer {
            binding.trackTimeMillsTrack.text = it
        })


        handler = Handler(Looper.getMainLooper())

        val track = intent.getParcelableExtra<Track>(TRACK_KEY)

        if (track != null) {
                preparePlayer()
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
    }

  companion object {
        private const val HALF_SECOND = 500L
    }

    @SuppressLint("SetTextI18n")
    private fun preparePlayer() {
        val track = intent.getParcelableExtra<Track>(TRACK_KEY)
        if (track != null) {
            viewModel.preparePlayer(track.previewUrl)
        }
        viewModel.callbackForCompletion {
            binding.timePlay.text = "00:00"
        }

        viewModel.callbackForPrepared {
            binding.buttonPlay.isEnabled = true
            playerState = PlayState.STATE_PREPARED
        }
    }

    private fun playbackControl() {
        viewModel.updateState()
       when(playerState) {
           PlayState.STATE_PLAYING -> {
               viewModel.pausePlayer()
               pausePlayer()
            }
           PlayState.STATE_PREPARED, PlayState.STATE_PAUSED -> {
               viewModel.startPlayer()
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
                    viewModel.updateState()
                    if (playerState == PlayState.STATE_PLAYING)
                    {
                       viewModel.updatePosition()
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
