package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.models.TracksState
import java.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private var trackList = ArrayList<Track>()
    private var storyList = ArrayList<Track>()
    private val adapter = TrackAdapter {
        viewModel.addToHistory(it)
    }
    private var searchText: String = ""
    private var textWatcher: TextWatcher? = null

    private val viewModel by viewModel<TracksSearchViewModel>()

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.observeState().observe(this) {
            render(it)
        }
        viewModel.storyListLiveData.observe(this, androidx.lifecycle.Observer {
            storyList = it
        })

        viewModel.checkStoryList()

        val currentView = this.window.decorView.rootView

        adapter.trackList = trackList

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.updateButton.setOnClickListener {
            viewModel.searchTextRequest(searchText)
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
            trackList.clear()
            adapter.notifyDataSetChanged()
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
                viewModel.searchTextRequest(searchText)
                true
            } else false
        }


        textWatcher =
            object : TextWatcher {
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
                    viewModel.searchDebounce(changedText = s?.toString() ?: "")
                }

                override fun afterTextChanged(s: Editable?) {
                }
            }
        textWatcher?.let { binding.inputEditText.addTextChangedListener(it) }


        binding.clearHistoryButton.setOnClickListener {
            storyList.clear()
            viewModel.writeStoryList(storyList)
            adapter.notifyDataSetChanged()
            binding.titleForStoryTextView.isVisible = false
            binding.clearHistoryButton.isVisible = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { binding.inputEditText.removeTextChangedListener(it) }
        viewModel.onCleared()

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
    }

    private fun render(state: TracksState) {
        when(state) {
            is TracksState.Loading -> showLoading()
            is TracksState.Error -> showError()
            is TracksState.Empty -> showEmpty()
            is TracksState.Content -> showContent(state.tracks)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(newTracksList: ArrayList<Track>) {
        binding.recyclerView.isVisible = true
        binding.progressBar.isVisible = false
        binding.placeholderNoInternet.isVisible = false
        binding.textNoInternet.isVisible = false
        binding.updateButton.isVisible = false
        binding.placeholderNothingWasFound.isVisible = false
        binding.textNothingWasFound.isVisible = false
        adapter.trackList.clear()
        adapter.trackList.addAll(newTracksList)
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showError() {
        trackList.clear()
        adapter.notifyDataSetChanged()
        binding.placeholderNoInternet.isVisible = true
        binding.textNoInternet.isVisible = true
        binding.updateButton.isVisible = true
        binding.placeholderNothingWasFound.isVisible = false
        binding.textNothingWasFound.isVisible = false
        binding.progressBar.isVisible = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showEmpty() {
        trackList.clear()
        adapter.notifyDataSetChanged()
        binding.placeholderNothingWasFound.isVisible = true
        binding.textNothingWasFound.isVisible = true
        binding.placeholderNoInternet.isVisible = false
        binding.textNoInternet.isVisible = false
        binding.updateButton.isVisible = false
        binding.progressBar.isVisible = false
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.placeholderNoInternet.isVisible = false
        binding.textNoInternet.isVisible = false
        binding.updateButton.isVisible = false
        binding.placeholderNothingWasFound.isVisible = false
        binding.textNothingWasFound.isVisible = false
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
}


