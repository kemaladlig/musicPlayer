package com.example.musicplayer.ui.screens

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.musicplayer.R
import com.example.musicplayer.data.Song
import com.example.musicplayer.data.SongRepository
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        currentSong?.let { song ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
                    .padding(24.dp)
            )
            {
                AlbumArtSection(viewModel, context)

            }

            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
            {
                SongInfoAndControlsSection(isPlaying, viewModel)

                Spacer(modifier = Modifier.height(8.dp))

                ProgressBarSection(sliderPosition, song.duration, viewModel)
            }

        } ?: NoSongSelectedMessage()
    }
}

@Composable
fun AlbumArtSection(viewModel: NowPlayingViewModel, context: Context) {
    val albumArtBitmap = remember(viewModel.currentSong.value?.albumArtUri) {
        viewModel.loadAlbumArt(context, viewModel.currentSong.value?.albumArtUri)
    }
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Album cover
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                if (albumArtBitmap != null) {
                    Image(
                        bitmap = albumArtBitmap.asImageBitmap(),
                        contentDescription = "Cover",
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.album),
                        contentDescription = "Default Cover",
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
}

@Composable
fun SongInfoAndControlsSection(isPlaying: Boolean, viewModel: NowPlayingViewModel) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        viewModel.currentSong.value?.let { song ->
            // Song Name
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
            // Song info
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF444444)),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = song.album,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF444444)),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(0.1f),
                horizontalAlignment = Alignment.Start
            ) {
                IconButton(onClick = { viewModel.restartSong() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_loop_24),
                        contentDescription = "Restart"
                    )
                }
            }

            Column(
                modifier = Modifier.weight(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { viewModel.playPreviousSong() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_skip_previous_24),
                            contentDescription = "Previous"
                        )
                    }

                    IconButton(onClick = {
                        if (isPlaying) {
                            viewModel.pauseSong()
                        } else {
                            viewModel.resumeSong()
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = if (isPlaying) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24),
                            contentDescription = if (isPlaying) "Pause" else "Play"
                        )
                    }

                    IconButton(onClick = { viewModel.playNextSong() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_skip_next_24),
                            contentDescription = "Next"
                        )
                    }
                }
            }
        }

    }


}

@Composable
fun ProgressBarSection(sliderPosition: Float, duration: Long, viewModel: NowPlayingViewModel) {
    val durationInSeconds = duration / 1000
    val currentPositionInSeconds = sliderPosition.toInt() / 1000

    Slider(
        value = sliderPosition,
        onValueChange = { newValue ->
            viewModel.mediaPlayer?.seekTo(newValue.toInt())
        },
        valueRange = 0f..duration.toFloat(),
        modifier = Modifier.fillMaxWidth()
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = formatDuration(currentPositionInSeconds.toLong()))
        Text(text = formatDuration(durationInSeconds))
    }
}

@Composable
fun NoSongSelectedMessage() {
    Text(
        text = "Choose a song to play from song list.",
        style = MaterialTheme.typography.bodyMedium
    )
}

@SuppressLint("DefaultLocale")
fun formatDuration(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%d:%02d", minutes, remainingSeconds)
}