package com.example.musicplayer.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicplayer.R
import com.example.musicplayer.viewmodel.NowPlayingViewModel
import kotlinx.coroutines.delay

@SuppressLint("RememberReturnType")
@Composable
fun NowPlayingScreen(modifier: Modifier = Modifier, viewModel: NowPlayingViewModel) {
    val isPlaying by viewModel.isPlaying.observeAsState(false)
    val currentSong by viewModel.currentSong.observeAsState()
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val context = LocalContext.current

    LaunchedEffect(currentSong) {
        while (true) {
            delay(1000L)
            viewModel.mediaPlayer?.let { mediaPlayer ->
                sliderPosition = mediaPlayer.currentPosition.toFloat()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .background(MaterialTheme.colorScheme.background)
    ) {

        currentSong?.let { song ->
            val albumArtBitmap = remember(viewModel.currentSong.value?.albumArtUri) {
                viewModel.loadAlbumArt(context, viewModel.currentSong.value?.albumArtUri)
            }
            if (albumArtBitmap != null) {
                Image(
                    bitmap = albumArtBitmap.asImageBitmap(),
                    contentDescription = "Cover",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.55f)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.album),
                    contentDescription = "Default Cover",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.55f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = song.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                Text(
                    text = song.album,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Controls
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { viewModel.playPreviousSong() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
                }

                IconButton(onClick = {
                    if (isPlaying) {
                        viewModel.pauseSong()
                    } else {
                        viewModel.resumeSong()
                    }
                }) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Lock else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play"
                    )
                }

                IconButton(onClick = { viewModel.playNextSong() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val durationInSeconds = song.duration / 1000
            val currentPositionInSeconds = sliderPosition.toInt() / 1000

            Slider(
                value = sliderPosition,
                onValueChange = { newValue ->
                    sliderPosition = newValue
                    viewModel.mediaPlayer?.seekTo(sliderPosition.toInt())
                },
                valueRange = 0f..song.duration.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formatDuration(currentPositionInSeconds.toLong()))
                Text(text = formatDuration(durationInSeconds))
            }
        } ?: Text(
            text = "Choose a song to play from song list.",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@SuppressLint("DefaultLocale")
fun formatDuration(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%d:%02d", minutes, remainingSeconds)
}