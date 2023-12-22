package com.example.playlistmaker.data.model

enum class PlayStateDto(val state: Int) {
    STATE_DEFAULT(0),
    STATE_PREPARED(1),
    STATE_PLAYING (2),
    STATE_PAUSED(3)
}
