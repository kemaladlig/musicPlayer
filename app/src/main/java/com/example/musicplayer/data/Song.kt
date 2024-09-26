package com.example.musicplayer.data

import kotlin.time.Duration

data class Song(
    val title: String,
    val artist: String,
    val album: String,
    val uri:String,
    val duration: Long,
    val albumArtUri: String
)