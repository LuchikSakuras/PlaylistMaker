package com.example.playlistmaker.ui.medialibrary

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.medialibrary.fragment.FavouritesFragment
import com.example.playlistmaker.ui.medialibrary.fragment.PlaylistsFragment

private const val ITEM_COUNT_FOR_LIBRARY = 2

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return ITEM_COUNT_FOR_LIBRARY
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavouritesFragment.newInstance()
            else -> PlaylistsFragment().newInstance()
        }
    }
}