package com.example.musicplayer.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicplayer.viewmodel.NowPlayingViewModel

@Composable
fun NowPlayingScreen(modifier: Modifier = Modifier, viewModel: NowPlayingViewModel) {
    val isPlaying = remember { mutableStateOf(false) }
    val currentSong by viewModel.currentSong.observeAsState()
    //val currentSong = ""

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        currentSong?.let {
            Text(text = "Şu anda çalan: ${it.title} - ${it.artist}")
        } ?: Text(text = "Şarkı bulunamadı.")
        Button(onClick = { isPlaying.value = !isPlaying.value }) {
            Text(text = if (isPlaying.value) "Pause" else "Play")
        }
    }
}