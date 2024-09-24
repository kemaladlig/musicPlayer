package com.example.musicplayer.navigation

import SongListScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.musicplayer.ui.screens.NowPlayingScreen


@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.SongListScreen.route) {
        composable(Screen.SongListScreen.route) { SongListScreen() }
        composable(Screen.NowPlayingScreen.route) { NowPlayingScreen() }
    }
}


sealed class Screen(val route: String) {
    data object SongListScreen : Screen("song_list")
    data object NowPlayingScreen : Screen("now_playing")
}