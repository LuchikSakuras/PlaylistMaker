package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrackAdapter
    private lateinit var buttonBack: LinearLayout
    private var searchText: String = ""
    private val urlNirvana = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
    private val urlJackson = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
    private val urlGees = "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
    private val urlZeppelin = "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
    private val urlRoses = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc4e6-3d8f-3ab7-7a12-95b7f2ee5e0e/UMG_cvrart_00602537535237_01_RGB72_1800x1800_13UAAIM74565.jpg/100x100bb.jpg"

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchText = searchEditText.text.toString()
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchText = savedInstanceState.getString(SEARCH_TEXT)
        searchEditText.setText(searchText)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.input_edit_text)
        clearButton = findViewById(R.id.clear_icon)
        recyclerView = findViewById(R.id.recyclerView)
        buttonBack = findViewById(R.id.button_back)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val currentView = this.window.decorView.rootView
        val artworkUrl100: ImageView =findViewById(R.id.artworkUrl100)

        Glide.with(applicationContext).load(urlNirvana).into(artworkUrl100)
        Glide.with(applicationContext).load(urlJackson).into(artworkUrl100)
        Glide.with(applicationContext).load(urlGees).into(artworkUrl100)
        Glide.with(applicationContext).load(urlZeppelin).into(artworkUrl100)
        Glide.with(applicationContext).load(urlRoses).into(artworkUrl100)


        val tracksList = ArrayList<Track>()
        tracksList.add(Track(resources.getString(R.string.track_name_nirvana), resources.getString(R.string.artist_name_nirvana), resources.getString(R.string.track_time_nirvana), urlNirvana))
        tracksList.add(Track(resources.getString(R.string.track_name_jackson), resources.getString(R.string.artist_name_jackson), resources.getString(R.string.track_time_jackson), urlJackson))
        tracksList.add(Track(resources.getString(R.string.track_name_gees), resources.getString(R.string.artist_name_gees), resources.getString(R.string.track_time_gees), urlGees))
        tracksList.add(Track(resources.getString(R.string.track_name_zeppelin), resources.getString(R.string.artist_name_zeppelin), resources.getString(R.string.track_time_zeppelin), urlZeppelin))
        tracksList.add(Track(resources.getString(R.string.track_name_roses), resources.getString(R.string.artist_name_roses), resources.getString(R.string.track_time_roses), urlRoses))
        adapter = TrackAdapter(tracksList)
        recyclerView.adapter = adapter




        if (savedInstanceState != null) {
            val searchText = savedInstanceState.getString(SEARCH_TEXT)
            searchEditText.setText(searchText)
        }

        buttonBack.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentView.windowToken, 0)
        }


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                if (s.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

    companion object {
        const val SEARCH_TEXT = "searchText"
    }
}

data class Track(val trackName: String, val artistName: String, val trackTime: String, val artworkUrl100: String)

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackNameView: TextView
    private val artistNameView: TextView
    private val trackTimeView: TextView
    private val artworkUrl100View: ImageView

    init {
        trackNameView = itemView.findViewById(R.id.trackName)
        artistNameView = itemView.findViewById(R.id.artistName)
        trackTimeView = itemView.findViewById(R.id.trackTime)
        artworkUrl100View = itemView.findViewById(R.id.artworkUrl100)
    }

    fun bind(track: Track) {
        trackNameView.text = track.trackName
        artistNameView.text = track.artistName
        trackTimeView.text = track.trackTime
        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(2))
            .into(artworkUrl100View)
   }
}

class TrackAdapter(
    private val tracksList: ArrayList<Track>
) : RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracksList[position])
    }

    override fun getItemCount() = tracksList.size
}
