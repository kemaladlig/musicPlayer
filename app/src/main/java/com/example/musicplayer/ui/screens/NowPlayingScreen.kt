package com.example.musicplayer.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NowPlayingScreen(modifier: Modifier = Modifier) {
    val isPlaying = remember { mutableStateOf(false) }
    val currentSong = "Seçili Şarkı: Test.mp3"

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(text = currentSong)
        Button(onClick = { isPlaying.value = !isPlaying.value }) {
            Text(text = if (isPlaying.value) "Durdur" else "Oynat")
        }
    }
}