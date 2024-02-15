package com.example.playlistmaker.ui.medialibrary

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.ui.medialibrary.viewModel.FavouritesFragmentViewModel
import com.example.playlistmaker.ui.medialibrary.viewModel.MediaLibraryViewModel
import com.example.playlistmaker.ui.medialibrary.viewModel.PlaylistsFragmentViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var tabMediator: TabLayoutMediator
    private lateinit var binding: ActivityMediaLibraryBinding
    private val viewModel by viewModel<MediaLibraryViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayot, binding.viewPager) { tab, position ->
            when(position){
                0 -> tab.text = "Избранные треки"
                else -> tab.text = "Плейлисты"
            }
        }

        tabMediator.attach()

        binding.buttonBack.setOnClickListener {
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}
