package com.example.musicplayer.data


import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore

object SongRepository {
    fun loadSongs(contentResolver: ContentResolver): List<Song> {
        val songList = mutableListOf<Song>()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )

        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val typeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)


            while (cursor.moveToNext()) {
                val songName = cursor.getString(nameColumn)
                val artist = cursor.getString(artistColumn)
                val album = cursor.getString(albumColumn)
                val mimeType = cursor.getString(typeColumn)
                val filePath = cursor.getString(dataColumn)
                val duration = cursor.getLong(durationColumn)
                val albumId = cursor.getLong(albumIdColumn)

                if (mimeType == "audio/mpeg" || mimeType == "audio/wav") {
                    songList.add(Song(songName.removeSuffix(".mp3").removeSuffix(".wav"),
                        artist,
                        album,
                        filePath,
                        duration,
                        albumArtUri = "content://media/external/audio/albumart/$albumId"
                    )
                    )
                }
            }
        }
        return songList
    }
}