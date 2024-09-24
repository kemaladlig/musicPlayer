package com.example.musicplayer

import android.Manifest
import android.os.Build
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun RequestPermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    val requiredPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionCheck = remember {
        ContextCompat.checkSelfPermission(context, requiredPermission)
    }

    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
        permissionGranted = true
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
    }

    LaunchedEffect(key1 = Unit) {
        if (!permissionGranted) {
            permissionLauncher.launch(requiredPermission)
        }
    }

    if (permissionGranted) {
        onPermissionGranted()
    }
}