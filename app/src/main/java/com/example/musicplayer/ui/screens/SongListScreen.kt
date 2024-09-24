package com.example.musicplayer.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SongListScreen(modifier: Modifier = Modifier) {
    val songs = listOf("Song 1", "Song 2", "Song 3") // Placeholder

    LazyColumn(modifier = modifier) {
        items(songs) { song ->
            Text(text = song)
        }
    }
}