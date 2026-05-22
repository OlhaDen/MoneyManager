package com.example.myapplication.ui.screens.splash

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.myapplication.R
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun SplashScreen(
    isUserLoggedIn: Boolean,
    isKnownDevice: Boolean,
    onNavigateToHome: () -> Unit,
    onNavigateToPin: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    val context = LocalContext.current
    
    // Funkcja pomocnicza do nawigacji
    val navigateNext = {
        when {
            isUserLoggedIn -> onNavigateToHome()
            isKnownDevice -> onNavigateToPin()
            else -> onNavigateToSignIn()
        }
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // Używamy bezpieczniejszego formatu URI
            val videoUri = Uri.parse("android.resource://${context.packageName}/raw/smartest_rick_roll")
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
        }
    }

    // Bezpiecznik: jeśli wideo nie skończy się w ciągu 6 sekund, przejdź dalej
    LaunchedEffect(Unit) {
        delay(6000)
        navigateNext()
    }

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    navigateNext()
                }
            }
            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                // Jeśli błąd wideo (np. ten plaintext), przejdź od razu dalej
                navigateNext()
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.release()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
