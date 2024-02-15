package com.example.playlistmaker.ui.medialibrary

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.medialibrary.viewModel.FavouritesFragmentViewModel
import com.example.playlistmaker.ui.medialibrary.viewModel.PlaylistsFragmentViewModel

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavouritesFragment.newInstance()
            else -> PlaylistsFragment().newInstance()
        }
    }
}