package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {

    fun shareLink(linkTheApp: String)

    fun openLink(linkOffer: String)

    fun openEmail(emailData: EmailData)

}