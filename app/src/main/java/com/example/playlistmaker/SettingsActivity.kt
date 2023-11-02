package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Switch
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    companion object {
        var currentTheme = R.style.Theme_MyApp
    }

    private lateinit var sharedPrefs: SharedPreferences
    private val userPreferences = UserPreferences()


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    lateinit var themeSwitcher: Switch


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(currentTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)


        val buttonBack: LinearLayout = findViewById(R.id.button_back)
        val buttonShareTheApp: LinearLayout = findViewById(R.id.button_share_the_app)
        val buttonSupport: LinearLayout = findViewById(R.id.button_support)
        val buttonArrowForward: LinearLayout = findViewById(R.id.button_arrow_forward)
        themeSwitcher = findViewById(R.id.themeSwitcher)

        if(userPreferences.readSwitcher(sharedPrefs)){
            themeSwitcher.isChecked = true
        }
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        buttonBack.setOnClickListener {
            finish()
        }

        buttonShareTheApp.setOnClickListener {
            val message = resources.getString(R.string.link_the_app)
            val shareIntent = Intent(Intent.ACTION_SEND)
            val shareTheApp = resources.getString(R.string.share_the_app)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(shareIntent, shareTheApp))
        }

        buttonSupport.setOnClickListener {
            val email = resources.getString(R.string.support_email)
            val title = resources.getString(R.string.support_title)
            val message = resources.getString(R.string.support_message)
            val uri: Uri = Uri.parse("mailto:$email")
                .buildUpon()
                .appendQueryParameter("to", email)
                .appendQueryParameter("subject", title)
                .appendQueryParameter("body", message)
                .build()
            val supportIntent = Intent(Intent.ACTION_SENDTO, uri)
            startActivity(Intent.createChooser(supportIntent, "subject"))
        }

        buttonArrowForward.setOnClickListener {
            val url = resources.getString(R.string.link_arrow_forward)
            val arrowForwardIntent = Intent(Intent.ACTION_VIEW)
            arrowForwardIntent.data = Uri.parse(url)
            startActivity(arrowForwardIntent)
        }
    }

    override fun onStop() {
        super.onStop()
        userPreferences.writeSwitcher(sharedPrefs, themeSwitcher.isChecked)
    }
}
