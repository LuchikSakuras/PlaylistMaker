package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.*

@Suppress("CAST_NEVER_SUCCEEDS", "DEPRECATION")
class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var arrowBack: ImageButton
    private lateinit var imageTrack: ImageView
    private lateinit var trackTitle: TextView
    private lateinit var artist: TextView
    private lateinit var buttonAdd: ImageButton
    private lateinit var buttonPlay: ImageButton
    private lateinit var buttonLike: ImageButton
    private lateinit var timePlay: TextView
    private lateinit var trackTimeMillsTrack: TextView
    private lateinit var collectionNameTrack: TextView
    private lateinit var releaseDateTrack: TextView
    private lateinit var primaryGenreNameTrack: TextView
    private lateinit var countryTrack: TextView
    private lateinit var collectionName: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_library)

        arrowBack = findViewById(R.id.arrowBack)
        imageTrack = findViewById(R.id.imageTrack)
        trackTitle = findViewById(R.id.trackName)
        artist = findViewById(R.id.artistName)
        buttonAdd = findViewById(R.id.buttonAdd)
        buttonPlay = findViewById(R.id.buttonPlay)
        buttonLike = findViewById(R.id.buttonLike)
        timePlay = findViewById(R.id.timePlay)
        trackTimeMillsTrack = findViewById(R.id.trackTimeMillsTrack)
        collectionNameTrack = findViewById(R.id.collectionNameTrack)
        releaseDateTrack = findViewById(R.id.releaseDateTrack)
        primaryGenreNameTrack = findViewById(R.id.primaryGenreNameTrack)
        countryTrack = findViewById(R.id.countryTrack)
        collectionName = findViewById(R.id.collectionName)

        val track: Track = Track(
            intent.getStringExtra("trackName").toString(),
            intent.getStringExtra("artistName").toString(),
            intent.getIntExtra("trackTimeMillis", 0),
            intent.getStringExtra("artworkUrl100").toString(),
            intent.getStringExtra("collectionName").toString(),
            intent.getStringExtra("releaseDate").toString(),
            intent.getStringExtra("primaryGenreName").toString(),
            intent.getStringExtra("country").toString()
        )



        trackTitle.text = track.trackName
        artist.text = track.artistName
        timePlay.text = "00:00"
        releaseDateTrack.text = track.releaseDate.substring(0, 4)
        primaryGenreNameTrack.text = track.primaryGenreName
        countryTrack.text = track.country
        Glide.with(imageTrack.context)
            .load(track.artworkUrl100.toString().replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .transform(
                FitCenter(),
                RoundedCorners(
                    imageTrack.resources.getDimensionPixelSize(R.dimen.dp_8)
                ),
            )
            .into(imageTrack)

        trackTimeMillsTrack.text =
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            )
                .format(track.trackTimeMillis.toLong())


        if (track.collectionName.isEmpty()) {
            collectionNameTrack.isVisible = false
            collectionName.isVisible = false
        } else {
            collectionNameTrack.isVisible = true
            collectionName.isVisible = true
            collectionNameTrack.text = track.collectionName
        }


        arrowBack.setOnClickListener {
            finish()
        }

    }


}