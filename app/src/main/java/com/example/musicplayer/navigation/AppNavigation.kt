package com.example.musicplayer.navigation

import SongListScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.musicplayer.ui.screens.NowPlayingScreen
import com.example.musicplayer.viewmodel.NowPlayingViewModel


@Composable
fun AppNavigation(navController: NavHostController, viewModel: NowPlayingViewModel) {
    NavHost(navController, startDestination = Screen.SongListScreen.route) {
        composable(Screen.SongListScreen.route) {
            SongListScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.NowPlayingScreen.route) {
            NowPlayingScreen(viewModel = viewModel)
        }
    }
}

sealed class Screen(val route: String) {
    data object SongListScreen : Screen("song_list")
    data object NowPlayingScreen : Screen("now_playing")
}