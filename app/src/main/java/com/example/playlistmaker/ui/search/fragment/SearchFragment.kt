package com.example.playlistmaker.ui.search.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.models.TracksState
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.ui.search.view_model.TracksSearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private var trackList = ArrayList<Track>()
    private var storyList = ArrayList<Track>()
    private val adapter = TrackAdapter {
        viewModel.addToHistory(it)
    }
    private var searchText: String = ""
    private var textWatcher: TextWatcher? = null

    private val viewModel by viewModel<TracksSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.storyListLiveData.observe(viewLifecycleOwner, Observer {
            storyList = it
        })

        viewModel.checkStoryList()

        val currentView = requireActivity().window.decorView.rootView

        adapter.trackList = trackList

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.updateButton.setOnClickListener {
            viewModel.searchTextRequest(searchText)
        }

        if (savedInstanceState != null) {
            val searchText = savedInstanceState.getString(SEARCH_TEXT)
            binding.inputEditText.setText(searchText)
        }

        binding.buttonClear.setOnClickListener {
            binding.inputEditText.setText("")
            val inputMethodManager = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as? InputMethodManager
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

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher?.let { binding.inputEditText.removeTextChangedListener(it) }
        viewModel.onCleared()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchText = binding.inputEditText.text.toString()
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val searchText = savedInstanceState?.getString(SEARCH_TEXT)
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