package com.example.musicplayer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.Song

class NowPlayingViewModel : ViewModel() {
    private val _songList = MutableLiveData<List<Song>>(emptyList())
    val songList: LiveData<List<Song>> get() = _songList

    private val _currentSong = MutableLiveData<Song?>()
    val currentSong: LiveData<Song?> get() = _currentSong

    fun setSongs(songs: List<Song>) {
        _songList.value = songs
    }

    fun playSong(song: Song) {
        _currentSong.value = song
    }
}