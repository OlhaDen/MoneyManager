package com.example.myapplication.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.theme.BlueGradientEnd
import com.example.myapplication.ui.theme.DarkBlue
import com.example.myapplication.ui.theme.DarkNavy

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onSignUpSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.authSuccess) {
        if (uiState.authSuccess) {
            onSignUpSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DarkNavy, DarkBlue)))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign Up",
            color = Color.White,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = pin,
            onValueChange = { if (it.length <= 4) pin = it.filter(Char::isDigit) },
            placeholder = { Text("PIN") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPin,
            onValueChange = { if (it.length <= 4) confirmPin = it.filter(Char::isDigit) },
            placeholder = { Text("Confirm PIN") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage ?: "",
                color = Color(0xFFFF8A8A),
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        Button(
            onClick = {
                viewModel.registerUser(
                    email = email.trim(),
                    pin = pin,
                    confirmPin = confirmPin
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BlueGradientEnd),
            enabled = !uiState.loading
        ) {
            if (uiState.loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Sign Up", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(
            onClick = onLoginClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Already have an account? Log in", color = Color(0xFFD8DEFF))
        }
    }
}