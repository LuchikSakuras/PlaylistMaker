package com.example.playlistmaker.ui.setting.activity

import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.PREFERENCES
import com.example.playlistmaker.UserPreferences
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.setting.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.ui.setting.SettingViewModelFactory
import com.example.playlistmaker.ui.setting.view_model.SettingsViewModel
import com.example.playlistmaker.util.Creator

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding
   // private lateinit var sharedPrefs: SharedPreferences
   // private val userPreferences = UserPreferences()


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this, SettingViewModelFactory(this))[SettingsViewModel::class.java]

        viewModel.checkLiveData.observe(this, androidx.lifecycle.Observer {
            binding.themeSwitcher.isChecked = it
        })

        //sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)

        binding.settingButtonBack.setOnClickListener {
            finish()
        }

        binding.buttonShareTheApp.setOnClickListener {
            viewModel.shareApp()
        }

        binding.buttonSupport.setOnClickListener {
            viewModel.openSupport()
        }

        binding.buttonArrowForward.setOnClickListener {
            viewModel.openTerms()
        }


       // binding.themeSwitcher.isChecked = userPreferences.readSwitcher(sharedPrefs)

        viewModel.themeSwitcherIsChecked()

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            viewModel.updateThemeSetting(ThemeSettings(checked))
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateThemeSetting(ThemeSettings(binding.themeSwitcher.isChecked))
    }
}
