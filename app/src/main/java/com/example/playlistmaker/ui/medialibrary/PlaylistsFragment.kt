package com.example.playlistmaker.ui.medialibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.ui.medialibrary.viewModel.FavouritesFragmentViewModel
import com.example.playlistmaker.ui.medialibrary.viewModel.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {

    private val viewModel by viewModel<PlaylistsFragmentViewModel>()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun newInstance(): Fragment {
        val fragment = PlaylistsFragment()
        return fragment
    }

}