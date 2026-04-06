package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.LightText

@Composable
fun GradientHeader(
    title: String,
    gradientColors: List<Color>,
    balance: String = "$0.00",
    subtitle: String = "All Time",
    income: String = "+$0.00",
    expenses: String = "-$0.00",
    icon: ImageVector,
    topContent: @Composable (RowScope.() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(gradientColors))
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (topContent != null) {
                    topContent()
                } else {
                    Text(
                        text = title,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.12f)
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Net Balance", color = LightText)
                            Text(
                                text = balance,
                                color = Color.White,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text(subtitle, color = LightText)
                        }

                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(Color.White.copy(alpha = 0.16f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Income", color = LightText)
                            Text(income, color = Color(0xFF66F0A3))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Expenses", color = LightText)
                            Text(expenses, color = Color(0xFFFF9F9F))
                        }
                    }
                }
            }
        }
    }
}