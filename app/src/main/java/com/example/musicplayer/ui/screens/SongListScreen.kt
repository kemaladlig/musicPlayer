import android.content.ContentResolver
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.musicplayer.RequestPermission
import com.example.musicplayer.components.SongCard
import com.example.musicplayer.data.Song
import com.example.musicplayer.data.SongRepository

@Composable
fun SongListScreen() {
    var songs by remember { mutableStateOf(listOf<Song>()) }
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver

    RequestPermission {
        songs = SongRepository.loadSongs(contentResolver)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Song List", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(songs) { song ->
                SongCard(song = song) {
                    // on click ve on long press eklenecek
                }
            }
        }
    }
}