package com.example.myapplication.ui.screens.splash

import android.media.MediaPlayer
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.BlueGradientEnd
import com.example.myapplication.ui.theme.BlueGradientStart
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    isUserLoggedIn: Boolean,
    isKnownDevice: Boolean,
    onNavigateToHome: () -> Unit,
    onNavigateToPin: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    val context = LocalContext.current

    // Odtwarzanie dźwięku powitalnego
    DisposableEffect(Unit) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.splash_sound)
        mediaPlayer?.start()

        onDispose {
            mediaPlayer?.release()
        }
    }

    // Opóźnienie przed nawigacją
    LaunchedEffect(Unit) {
        delay(2500)
        when {
            isUserLoggedIn -> onNavigateToHome()
            isKnownDevice -> onNavigateToPin()
            else -> onNavigateToSignIn()
        }
    }

    // Animacja pulsującego logo
    val transition = rememberInfiniteTransition(label = "splash_anim")
    val scale = transition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(BlueGradientStart, BlueGradientEnd))
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Białe koło z ikoną portfela (logo)
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountBalanceWallet,
                    contentDescription = null,
                    tint = BlueGradientEnd,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "MoneyManager",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Manage your finances with ease",
                color = Color(0xFFDDE4FF),
                fontSize = 16.sp
            )
        }
    }
}
