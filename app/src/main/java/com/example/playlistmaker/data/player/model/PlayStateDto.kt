package com.example.playlistmaker.data.player.model

import com.example.playlistmaker.domain.player.models.PlayState

enum class PlayStateDto(val state: Int) {
    STATE_DEFAULT(0),
    STATE_PREPARED(1),
    STATE_PLAYING (2),
    STATE_PAUSED(3)
}