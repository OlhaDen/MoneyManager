package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AudioPlaceholderCard() {
    var isPlaying by remember { mutableStateOf(false) }

    Card {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Audio Guide",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFFEAF7FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.GraphicEq,
                        contentDescription = null,
                        tint = Color(0xFF4F7DFF)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Expense Tracker Audio Tutorial",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Placeholder for mp3 / audio file",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Outlined.Pause else Icons.Outlined.PlayArrow,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isPlaying) "Pause" else "Play")
                }

                OutlinedButton(
                    onClick = { isPlaying = false },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Stop")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Na potrzeby projektu możesz zostawić placeholder bez prawdziwego pliku audio albo później dodać mp3 do res/raw.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}