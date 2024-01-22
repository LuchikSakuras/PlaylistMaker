package com.example.playlistmaker.ui.main.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.main.MainViewModel
import com.example.playlistmaker.ui.main.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]

        viewModel.updateTheme()

        binding.buttonSearch.setOnClickListener {
            viewModel.openSearch()
        }

        binding.buttonMediaLibrary.setOnClickListener {
            viewModel.openMediaLibrary()
        }

        binding.buttonSettings.setOnClickListener {
            viewModel.openSettings()
        }

    }
}

