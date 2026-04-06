package com.example.myapplication.ui.screens.splash

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.BlueGradientEnd
import com.example.myapplication.ui.theme.BlueGradientStart
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    isLoggedIn: Boolean,
    onNavigateToHome: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2500)
        if (isLoggedIn) {
            onNavigateToHome()
        } else {
            onNavigateToSignIn()
        }
    }

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
                Brush.verticalGradient(
                    listOf(BlueGradientStart, BlueGradientEnd)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                text = "Expense Tracker",
                color = Color.White,
                fontSize = 30.sp,
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