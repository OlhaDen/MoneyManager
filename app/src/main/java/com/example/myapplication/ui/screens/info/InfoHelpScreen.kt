package com.example.myapplication.ui.screens.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.components.VideoPlaceholderCard
import com.example.myapplication.ui.theme.InfoBackground
import com.example.myapplication.ui.theme.TealEnd
import com.example.myapplication.ui.theme.TealStart

@Composable
fun InfoHelpScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(InfoBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(TealStart, TealEnd)))
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Outlined.ArrowBack, null, tint = Color.White)
                }
                Icon(Icons.Outlined.Info, null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Information & Help",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            VideoPlaceholderCard()

            Card {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Contact Support", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Have questions or need help? We're here to assist you!")

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Email, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "support@expensetracker.com",
                            color = Color(0xFF009688)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "We typically respond within 24 hours during business days",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Card {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Key Features", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    FeatureRow(
                        icon = Icons.Outlined.Wallet,
                        title = "Track Expenses & Income",
                        description = "Record all your transactions with categories and view detailed breakdowns"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FeatureRow(
                        icon = Icons.Outlined.Payments,
                        title = "Scheduled Payments",
                        description = "Set up recurring bills and never miss a payment deadline"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FeatureRow(
                        icon = Icons.Outlined.ShowChart,
                        title = "Visual Analytics",
                        description = "See your spending patterns with charts and graphs"
                    )
                }
            }

            Card {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("About", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Version:")
                        Text("1.0.0")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Last Updated:")
                        Text("March 27, 2026")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "© 2026 Expense Tracker. All rights reserved.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun FeatureRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row {
        Icon(icon, contentDescription = null, tint = Color(0xFF4F7DFF))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
        }
    }
}