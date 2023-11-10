package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class SearchActivity : AppCompatActivity(){

    private var trackList = ArrayList<Track>()
    private var storyList = ArrayList<Track>()

    private val adapter = TrackAdapter{
       // goToLibrary(it)
       addToHistory(it)
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                ).build()
        )
        .build()
    private val api = retrofit.create(ITunesApi::class.java)

    private var searchText: String = ""
    private lateinit var placeholderNoInternet: LinearLayout
    private lateinit var nothingWasFound: LinearLayout
    private lateinit var updateButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var clearHistoryButton: Button
    private lateinit var titleForStoryTextView: TextView
    private lateinit var sharedPrefs: SharedPreferences
    private val userPreferences = UserPreferences()

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val currentView = this.window.decorView.rootView
        val clearButton: ImageView = findViewById(R.id.clearIcon)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val buttonBack: ImageView = findViewById(R.id.buttonBack)

        placeholderNoInternet = findViewById(R.id.placeholder_no_internet)
        nothingWasFound = findViewById(R.id.nothing_was_found)
        updateButton = findViewById(R.id.update_button)
        searchEditText = findViewById(R.id.inputEditText)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        titleForStoryTextView = findViewById(R.id.titleForStoryTextView)
        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        storyList = userPreferences.readStoryList(sharedPrefs)

        adapter.trackList = trackList
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        updateButton.setOnClickListener {
            searchTextRequest()
        }

        if (savedInstanceState != null) {
            val searchText = savedInstanceState.getString(SEARCH_TEXT)
            searchEditText.setText(searchText)
        }

        buttonBack.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentView.windowToken, 0)
            trackListClear()
        }

        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (searchEditText.hasFocus() && storyList.isNotEmpty()){
                showStory()
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            } else {
                closeStory()
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            //кнопка переноса строки будет заменена на кнопку Done:
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTextRequest()
                true
            } else
                false
        }

        searchEditText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    searchText = searchEditText.text.toString()
                    clearButton.isVisible = !s.isNullOrEmpty()

                    if (searchEditText.hasFocus() && s?.isEmpty()== true && storyList.isNotEmpty()){
                        showStory()
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    } else {
                        closeStory()
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })

        clearHistoryButton.setOnClickListener {
            storyList.clear()
            userPreferences.writeStoryList(sharedPrefs, storyList)
            adapter.notifyDataSetChanged()
            titleForStoryTextView.isVisible = false
            clearHistoryButton.isVisible = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchText = findViewById<EditText>(R.id.inputEditText).text.toString()
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchText = savedInstanceState.getString(SEARCH_TEXT)
        findViewById<EditText>(R.id.inputEditText).setText(searchText)
    }

    private companion object {
        const val SEARCH_TEXT = "searchText"
    }

    private fun showPlaceholderError() {
        trackListClear()
        placeholderNoInternet.isVisible = true
        nothingWasFound.isVisible = false
    }

    private fun showPlaceholderForEmpty() {
        trackListClear()
        nothingWasFound.isVisible = true
        placeholderNoInternet.isVisible = false
    }

    private fun closePlaceholder() {
        placeholderNoInternet.isVisible = false
        nothingWasFound.isVisible = false
    }

    private fun searchTextRequest() {
        if (searchEditText.text.isNotEmpty()) {
            api.search(searchEditText.text.toString())
                .enqueue(object : Callback<TrackResult> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TrackResult>, response: Response<TrackResult>,
                    ) {
                        if (response.code() == 200) {
                            trackListClear()
                            closePlaceholder()
                            val tracksResults = response.body()?.results
                            if (tracksResults?.isNotEmpty() == true) {
                                trackList.addAll(tracksResults)
                                adapter.notifyDataSetChanged()
                            } else {
                                showPlaceholderForEmpty()
                            }

                        } else {
                            showPlaceholderError()

                        }
                    }

                    override fun onFailure(call: Call<TrackResult>, t: Throwable) {
                        showPlaceholderError()
                    }
                })
        } else {
            trackListClear()
        }
    }

   @SuppressLint("NotifyDataSetChanged")
   private fun addToHistory(track: Track) {
        if (!storyList.contains(track)){
            if (storyList.size == 10) {
                storyList.removeLast()
                    goToLibrary(track)
            } else {
                goToLibrary(track)
            }
        } else {
            storyList.remove(track)
            goToLibrary(track)
        }
    }

    private fun goToLibrary(track: Track){
        addStoryList(track)
        val intent = Intent(this@SearchActivity, MediaLibraryActivity::class.java)
        intent.putExtra("trackName", track.trackName)
        Log.e("trackname", track.trackName)
        intent.putExtra("artistName", track.artistName)
        intent.putExtra("trackTimeMillis", track.trackTimeMillis)
        intent.putExtra("artworkUrl100", track.artworkUrl100)
        intent.putExtra("collectionName", track.collectionName)
        intent.putExtra("releaseDate", track.releaseDate)
        intent.putExtra("primaryGenreName", track.primaryGenreName)
        intent.putExtra("country", track.country)
        startActivity(intent)
    }

    private fun showStory(){
        titleForStoryTextView.isVisible = true
        clearHistoryButton.isVisible = true
        adapter.trackList = storyList
    }

    private fun closeStory(){
        titleForStoryTextView.isVisible = false
        clearHistoryButton.isVisible = false
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


