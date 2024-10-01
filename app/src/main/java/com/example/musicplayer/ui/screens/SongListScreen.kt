import android.content.ContentResolver
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.musicplayer.RequestPermission
import com.example.musicplayer.components.SongCard
import com.example.musicplayer.data.Song
import com.example.musicplayer.data.SongRepository
import com.example.musicplayer.navigation.Screen
import com.example.musicplayer.viewmodel.NowPlayingViewModel

@Composable
fun SongListScreen(navController: NavHostController, viewModel: NowPlayingViewModel) {
    var songs by remember { mutableStateOf(listOf<Song>()) }
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver
    var showDialog by remember { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }

    fun loadSongs() {
        songs = SongRepository.loadSongs(contentResolver)
        viewModel.setSongs(songs)
    }

    RequestPermission {
        LaunchedEffect(Unit) {
            loadSongs()
        }
    }

    if (showDialog && selectedSong != null) {
        SongOptionsDialog(
            song = selectedSong!!,
            onRename = { /* İsim değiştirme işlemi */ },
            onDelete = { /* Silme işlemi */ },
            onPlay = {
                viewModel.playSong(selectedSong!!)
                navController.navigate(Screen.NowPlayingScreen.route)
            },
            onDismiss = { showDialog = false }
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Song List", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(songs) { song ->
                SongCard(
                    song = song,
                    onClick = {
                        viewModel.playSong(song)
                        navController.navigate(Screen.NowPlayingScreen.route)
                    },
                    onLongClick = {
                        selectedSong = song
                        showDialog = true
                    }
                )
            }
        }
    }
}


@Composable
fun SongOptionsDialog(song: Song, onRename: () -> Unit, onDelete: () -> Unit, onPlay: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        text = {
            Column(modifier = Modifier.padding(2.dp)) {
                TextButton(onClick = { onPlay(); onDismiss() }) {
                    Text("Play", style = MaterialTheme.typography.bodyLarge)
                }
                TextButton(onClick = { onRename(); onDismiss() }) {
                    Text("Rename", style = MaterialTheme.typography.bodyLarge)
                }
                TextButton(onClick = { onDelete() }) {
                    Text("Delete", style = MaterialTheme.typography.bodyLarge)
                }
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancel", style = MaterialTheme.typography.bodyLarge)
                }
            }
        },
        confirmButton = {}
    )
}