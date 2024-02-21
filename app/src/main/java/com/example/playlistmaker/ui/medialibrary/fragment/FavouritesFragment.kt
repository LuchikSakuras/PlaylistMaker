package com.example.playlistmaker.ui.medialibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavouritesBinding
import com.example.playlistmaker.ui.medialibrary.viewModel.FavouritesFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment: Fragment() {

    private val viewModel by viewModel<FavouritesFragmentViewModel>()

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): Fragment = FavouritesFragment()
    }
}