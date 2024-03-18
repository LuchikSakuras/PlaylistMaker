package com.example.playlistmaker.ui.setting.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.FrafmentSettingsBinding
import com.example.playlistmaker.domain.setting.model.ThemeSettings
import com.example.playlistmaker.ui.setting.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FrafmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrafmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.checkLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.themeSwitcher.isChecked = it
        })

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
                (activity?.applicationContext as App).switchTheme(checked)
                viewModel.updateThemeSetting(ThemeSettings(checked))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateThemeSetting(ThemeSettings(binding.themeSwitcher.isChecked))
    }

}