package com.example.musicplayer.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.Song

class NowPlayingViewModel : ViewModel() {
    private val _songList = MutableLiveData<List<Song>>(emptyList())
    val songList: LiveData<List<Song>> get() = _songList

    var mediaPlayer: MediaPlayer? = null

    private val _currentSong = MutableLiveData<Song?>()
    val currentSong: LiveData<Song?> get() = _currentSong

    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    private var isPaused = false

    fun setSongs(songs: List<Song>) {
        _songList.value = songs
    }

    fun loadAlbumArt(context: Context, albumArtUri: String?): Bitmap? {
        return try {
            if (albumArtUri != null) {
                val uri = Uri.parse(albumArtUri)
                val inputStream = context.contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(inputStream)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun playSong(song: Song) {
        if (mediaPlayer != null) {
            mediaPlayer?.release()
        }
        mediaPlayer = MediaPlayer().apply {
            setDataSource(song.uri)
            prepare()
            start()
            _isPlaying.value = true
            _currentSong.value = song
        }
    }
    fun resumeSong() {
        if (isPaused && mediaPlayer != null) {
            mediaPlayer?.start()
            _isPlaying.value = true
            isPaused = false
        }
    }
    fun pauseSong() {
        mediaPlayer?.pause()
        _isPlaying.value = false
        isPaused = true
    }
    fun stopSong() {
        mediaPlayer?.stop()
        _isPlaying.value = false
        isPaused = false
    }

    fun playPreviousSong() {
        val currentIndex = _songList.value?.indexOf(_currentSong.value)
        if (currentIndex != null && currentIndex > 0) {
            _songList.value?.get(currentIndex - 1)?.let { playSong(it) }
        }
    }

    fun playNextSong() {
        val currentIndex = _songList.value?.indexOf(_currentSong.value)
        if (currentIndex != null && currentIndex < (_songList.value?.size ?: 0) - 1) {
            _songList.value?.get(currentIndex + 1)?.let { playSong(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
    }
}