package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    companion object {
        var currentTheme = R.style.Theme_MyApp
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(currentTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack: LinearLayout = findViewById<LinearLayout>(R.id.buttonBack)
        val buttonShareTheApp: LinearLayout = findViewById<LinearLayout>(R.id.buttonShareTheApp)
        val buttonSupport: LinearLayout = findViewById<LinearLayout>(R.id.buttonSupport)
        val buttonArrowForward: LinearLayout = findViewById<LinearLayout>(R.id.buttonArrowForward)
        val buttonDarkTheme: LinearLayout = findViewById<LinearLayout>(R.id.buttonDarkTheme)


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

        buttonDarkTheme.setOnClickListener{
            currentTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (currentTheme){
                Configuration.UI_MODE_NIGHT_NO -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }
}
