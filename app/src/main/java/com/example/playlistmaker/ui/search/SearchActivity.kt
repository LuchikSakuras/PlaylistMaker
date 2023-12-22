package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.data.network.ITunesApi
import com.example.playlistmaker.PREFERENCES
import com.example.playlistmaker.TRACK_KEY
import com.example.playlistmaker.data.model.TrackResponse
import com.example.playlistmaker.UserPreferences
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.audioplayer.AudioPlayerActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class SearchActivity : AppCompatActivity() {

    private var trackList = ArrayList<Track>()
    private var storyList = ArrayList<Track>()

    private val adapter = TrackAdapter {
        addToHistory(it)
    }

    private val retrofit = Retrofit.Builder().baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create()).client(
            OkHttpClient.Builder().addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            ).build()
        ).build()
    private val api = retrofit.create(ITunesApi::class.java)

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTextRequest() }

    private var searchText: String = ""
    private lateinit var sharedPrefs: SharedPreferences
    private val userPreferences = UserPreferences()
    private lateinit var binding: ActivitySearchBinding

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentView = this.window.decorView.rootView

        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        storyList = userPreferences.readStoryList(sharedPrefs)
        adapter.trackList = trackList

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.updateButton.setOnClickListener {
            searchTextRequest()
        }

        if (savedInstanceState != null) {
            val searchText = savedInstanceState.getString(SEARCH_TEXT)
            binding.inputEditText.setText(searchText)
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.buttonClear.setOnClickListener {
            binding.inputEditText.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentView.windowToken, 0)
            trackListClear()
        }

        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (binding.inputEditText.hasFocus() && storyList.isNotEmpty()) {
                showStory()
                binding.recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            } else {
                closeStory()
                binding.recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            //кнопка переноса строки будет заменена на кнопку Done:
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTextRequest()
                true
            } else false
        }

        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = binding.inputEditText.text.toString()
                binding.buttonClear.isVisible = !s.isNullOrEmpty()

                if (binding.inputEditText.hasFocus() && s?.isEmpty() == true && storyList.isNotEmpty()) {
                    showStory()
                    binding.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                } else {
                    closeStory()
                    binding.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.clearHistoryButton.setOnClickListener {
            storyList.clear()
            userPreferences.writeStoryList(sharedPrefs, storyList)
            adapter.notifyDataSetChanged()
            binding.titleForStoryTextView.isVisible = false
            binding.clearHistoryButton.isVisible = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchText = binding.inputEditText.text.toString()
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchText = savedInstanceState.getString(SEARCH_TEXT)
        binding.inputEditText.setText(searchText)
    }

    private companion object {
        const val SEARCH_TEXT = "searchText"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun showPlaceholderError() {
        trackListClear()
        binding.placeholderNoInternet.isVisible = true
        binding.textNoInternet.isVisible = true
        binding.updateButton.isVisible = true
        binding.placeholderNothingWasFound.isVisible = false
        binding.textNothingWasFound.isVisible = false
    }

    private fun showPlaceholderForEmpty() {
        trackListClear()
        binding.placeholderNothingWasFound.isVisible = true
        binding.textNothingWasFound.isVisible = true
        binding.placeholderNoInternet.isVisible = false
        binding.textNoInternet.isVisible = false
        binding.updateButton.isVisible = false
    }

    private fun closePlaceholder() {
        binding.placeholderNoInternet.isVisible = false
        binding.textNoInternet.isVisible = false
        binding.placeholderNothingWasFound.isVisible = false
        binding.textNothingWasFound.isVisible = false
        binding.updateButton.isVisible = false
    }

    private fun searchTextRequest() {
        if (binding.inputEditText.text.isNotEmpty()) {
            binding.progressBar.isVisible = true
            api.search(binding.inputEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TrackResponse>, response: Response<TrackResponse>,
                    ) {
                        binding.progressBar.isVisible = false
                        if (response.code() == 200) {
                            trackListClear()
                            closePlaceholder()
                            val tracksResults = response.body()?.results
                            if (tracksResults?.isNotEmpty() == true) {
                                trackList.addAll(tracksResults)
                                adapter.notifyDataSetChanged()
                            } else {
                                binding.progressBar.isVisible = false
                                showPlaceholderForEmpty()
                            }

                        } else {
                            binding.progressBar.isVisible = false
                            showPlaceholderError()
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        binding.progressBar.isVisible = false
                        showPlaceholderError()
                    }
                })
        } else {
            trackListClear()
            closePlaceholder()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addToHistory(track: Track) {
        if (!storyList.contains(track)) {
            if (storyList.size == 10) {
                storyList.removeLast()
                goToPlayer(track)
            } else {
                goToPlayer(track)
            }
        } else {
            storyList.remove(track)
            goToPlayer(track)
        }
    }

    private fun goToPlayer(track: Track) {
        addStoryList(track)

        val trackData = Track(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )

        if (clickDebounce()) {
            val intent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, trackData)
            startActivity(intent)
        }
    }

    private fun showStory() {
        binding.titleForStoryTextView.isVisible = true
        binding.clearHistoryButton.isVisible = true
        adapter.trackList = storyList
    }

    private fun closeStory() {
        binding.titleForStoryTextView.isVisible = false
        binding.clearHistoryButton.isVisible = false
        adapter.trackList = trackList
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addStoryList(track: Track) {
        storyList.add(0, track)
        adapter.notifyDataSetChanged()
        userPreferences.writeStoryList(sharedPrefs, storyList)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun trackListClear() {
        trackList.clear()
        adapter.notifyDataSetChanged()
    }

}


