package com.example.playlistmaker.ui.setting.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.App
import com.example.playlistmaker.R
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
            val shareTheApp = getString(R.string.share_the_app)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.link_the_app))
            val chooserIntent = Intent.createChooser(shareIntent, shareTheApp)
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(chooserIntent)
        }

        binding.buttonSupport.setOnClickListener {
            val uri: Uri =
                Uri.parse("mailto:")
                    .buildUpon()
                    .appendQueryParameter("to", getString(R.string.support_email))
                    .appendQueryParameter("subject", getString(R.string.support_title))
                    .appendQueryParameter("body", getString(R.string.support_message))
                    .build()
            val supportIntent = Intent(Intent.ACTION_SENDTO, uri)
            supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(Intent.createChooser(supportIntent, "subject"))
        }

        binding.buttonArrowForward.setOnClickListener {
            val arrowForwardIntent = Intent(Intent.ACTION_VIEW)
            arrowForwardIntent.data = Uri.parse(getString(R.string.link_arrow_forward))
            arrowForwardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(arrowForwardIntent)
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