package com.example.playlistmaker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
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

class SearchActivity : AppCompatActivity() {

    private var trackList = ArrayList<Track>()
    private val adapter = TrackAdapter(trackList)
    private var searchText: String = ""
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
    private lateinit var placeholderNoInternet: LinearLayout
    private lateinit var nothingWasFound: LinearLayout
    private lateinit var updateButton: Button
    private lateinit var searchEditText: EditText


    @SuppressLint("MissingInflatedId")
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

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            //кнопка переноса строки будет заменена на кнопку Done:
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTextRequest()
                true
            } else
                false
        }

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
                    if (s.isNullOrEmpty()) {
                        clearButton.visibility = View.GONE
                    } else {
                        clearButton.visibility = View.VISIBLE
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

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

    companion object {
        const val SEARCH_TEXT = "searchText"
    }

    private fun showPlaceholderError() {
        trackListClear()
        placeholderNoInternet.visibility = View.VISIBLE
        nothingWasFound.visibility = View.GONE
    }

    private fun showPlaceholderForEmpty() {
        trackListClear()
        nothingWasFound.visibility = View.VISIBLE
        placeholderNoInternet.visibility = View.GONE
    }

    private fun closePlaceholder() {
        placeholderNoInternet.visibility = View.GONE
        nothingWasFound.visibility = View.GONE
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
    private fun trackListClear() {
        trackList.clear()
        adapter.notifyDataSetChanged()
    }
}

