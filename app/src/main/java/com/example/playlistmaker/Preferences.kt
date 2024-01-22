package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.domain.search.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserPreferences {

    fun readStoryList(sharedPreferences: SharedPreferences): ArrayList<Track> {
        val gson = Gson()
        val json = sharedPreferences.getString(TRACK_LIST_KEY, null)
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson(json, type) ?: ArrayList()
    }

    fun writeStoryList(sharedPreferences: SharedPreferences, storyList: ArrayList<Track>) {
        val json = Gson().toJson(storyList)
        sharedPreferences.edit().putString(TRACK_LIST_KEY, json).apply()
    }

    fun readSwitcher(sharedPreferences: SharedPreferences): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME_KEY, false)
    }

    fun writeSwitcher(sharedPreferences: SharedPreferences, switch: Boolean) {
        sharedPreferences.edit().putBoolean(DARK_THEME_KEY, switch).apply()
    }
}