package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import java.util.*

class TrackViewHolder(view: View) : RecyclerView.ViewHolder(
    view
) {
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTimeMillis: TextView = itemView.findViewById(R.id.track_time)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.artworkUrl100)

    @SuppressLint("ResourceType")
    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTimeMillis.text = SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(track.trackTimeMillis.toLong())
        Glide.with(itemView.context).load(track.artworkUrl100).placeholder(R.drawable.placeholder)
            .transform(
                FitCenter(),
                RoundedCorners(
                    itemView.resources.getDimensionPixelSize(R.dimen.dp_2)
                ),
            ).into(artworkUrl100)
    }
}

