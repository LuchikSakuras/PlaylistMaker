package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPrefs: SharedPreferences
    private val userPreferences = UserPreferences()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)

        binding.settingButtonBack.setOnClickListener {
            finish()
        }

        binding.buttonShareTheApp.setOnClickListener {
            val message = resources.getString(R.string.link_the_app)
            val shareIntent = Intent(Intent.ACTION_SEND)
            val shareTheApp = resources.getString(R.string.share_the_app)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(shareIntent, shareTheApp))
        }

        binding.buttonSupport.setOnClickListener {
            val email = resources.getString(R.string.support_email)
            val title = resources.getString(R.string.support_title)
            val message = resources.getString(R.string.support_message)
            val uri: Uri = Uri.parse("mailto:$email").buildUpon().appendQueryParameter("to", email)
                .appendQueryParameter("subject", title).appendQueryParameter("body", message)
                .build()
            val supportIntent = Intent(Intent.ACTION_SENDTO, uri)
            startActivity(Intent.createChooser(supportIntent, "subject"))
        }

        binding.buttonArrowForward.setOnClickListener {
            val url = resources.getString(R.string.link_arrow_forward)
            val arrowForwardIntent = Intent(Intent.ACTION_VIEW)
            arrowForwardIntent.data = Uri.parse(url)
            startActivity(arrowForwardIntent)
        }

        binding.themeSwitcher.isChecked = userPreferences.readSwitcher(sharedPrefs)

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            userPreferences.writeSwitcher(sharedPrefs, checked)
        }
    }

    override fun onStop() {
        super.onStop()
        userPreferences.writeSwitcher(sharedPrefs, binding.themeSwitcher.isChecked)
    }
}
