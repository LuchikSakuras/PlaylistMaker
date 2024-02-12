package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareLink(linkTheApp: String) {
        val shareTheApp = context.getString(R.string.share_the_app)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, linkTheApp)
        val chooserIntent = Intent.createChooser(shareIntent, shareTheApp)
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    override fun openEmail(emailData: EmailData) {
        val uri: Uri =
            Uri.parse("mailto:${emailData.email}")
                .buildUpon()
                .appendQueryParameter("to", emailData.email)
                .appendQueryParameter("subject", emailData.title)
                .appendQueryParameter("body", emailData.message)
                .build()
        val supportIntent = Intent(Intent.ACTION_SENDTO, uri)
        supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(Intent.createChooser(supportIntent, "subject"))
    }

    override fun openLink(linkOffer: String) {
        val arrowForwardIntent = Intent(Intent.ACTION_VIEW)
        arrowForwardIntent.data = Uri.parse(linkOffer)
        arrowForwardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(arrowForwardIntent)
    }
}