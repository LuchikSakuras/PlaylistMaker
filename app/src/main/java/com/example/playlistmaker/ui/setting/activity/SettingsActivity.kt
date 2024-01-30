package com.example.playlistmaker.ui.setting.activity


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.setting.model.ThemeSettings
import com.example.playlistmaker.ui.setting.SettingViewModelFactory
import com.example.playlistmaker.ui.setting.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this, SettingViewModelFactory(this))[SettingsViewModel::class.java]

        viewModel.checkLiveData.observe(this, androidx.lifecycle.Observer {
            binding.themeSwitcher.isChecked = it
        })


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
