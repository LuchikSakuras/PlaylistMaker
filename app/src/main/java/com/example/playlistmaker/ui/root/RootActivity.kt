package com.example.playlistmaker.ui.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.ui.medialibrary.fragment.MediaLibraryFragment
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.example.playlistmaker.ui.setting.fragment.SettingsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("DEPRECATION")
class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    private val viewModel by viewModel<RootViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.searchFragment -> replaceFragment(SearchFragment())
                R.id.mediaLibraryFragment -> replaceFragment(MediaLibraryFragment())
                R.id.settingsFragment -> replaceFragment(SettingsFragment())
            }
            true
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                this.add(R.id.container_view, MediaLibraryFragment())
            }
            binding.bottomNavigationView.menu.findItem(R.id.mediaLibraryFragment).isChecked = true
            viewModel.updateTheme()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_view, fragment)
            .commit()
    }
}


