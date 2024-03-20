package com.example.playlistmaker.domain.player.models

enum class PlayState(val state: Int) {
    STATE_DEFAULT(0),
    STATE_PREPARED(1),
    STATE_PLAYING (2),
    STATE_PAUSED(3)
}