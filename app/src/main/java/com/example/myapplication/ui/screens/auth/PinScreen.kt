package com.example.myapplication.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.theme.DarkBlue
import com.example.myapplication.ui.theme.DarkNavy

@Composable
fun PinScreen(
    email: String,
    viewModel: AuthViewModel,
    onBackClick: () -> Unit,
    onPinComplete: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.authSuccess) {
        if (uiState.authSuccess) {
            onPinComplete()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage == "Incorrect PIN") {
            pin = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DarkNavy, DarkBlue)))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Enter Your PIN",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = email,
            color = Color(0xFFD8DEFF)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .border(
                            width = 2.dp,
                            color = if (index < pin.length) Color.White else Color.Gray,
                            shape = CircleShape
                        )
                        .background(
                            color = if (index < pin.length) Color.White else Color.Transparent,
                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage ?: "",
                color = Color(0xFFFF8A8A)
            )
        }

        if (uiState.loading) {
            Spacer(modifier = Modifier.height(12.dp))
            CircularProgressIndicator(color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        val rows = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9")
        )

        rows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                row.forEach { number ->
                    PinKey(
                        text = number,
                        enabled = !uiState.loading
                    ) {
                        if (pin.length < 4) {
                            pin += number
                            if (pin.length == 3) {
                                // nic
                            }
                            if (pin.length == 4) {
                                viewModel.verifyPin(email, pin)
                            }
                        }
                    }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Spacer(modifier = Modifier.width(88.dp))

            PinKey(
                text = "0",
                enabled = !uiState.loading
            ) {
                if (pin.length < 4) {
                    pin += "0"
                    if (pin.length == 4) {
                        viewModel.verifyPin(email, pin)
                    }
                }
            }

            Button(
                onClick = {
                    if (pin.isNotEmpty()) pin = pin.dropLast(1) else onBackClick()
                },
                modifier = Modifier.size(width = 88.dp, height = 70.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B4F87)
                ),
                enabled = !uiState.loading
            ) {
                Text("Back")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Forgot Password?",
            color = Color(0xFF6F8EFF)
        )
    }
}

@Composable
private fun PinKey(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(width = 88.dp, height = 70.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF2F6FB)
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 26.sp
        )
    }
}