package com.example.myapplication.ui.screens.history

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.screens.home.BalanceHistoryData
import com.example.myapplication.ui.screens.home.HomeViewModel
import com.example.myapplication.ui.theme.BlueGradientStart
import com.example.myapplication.ui.theme.HomeBackground
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceChartScreen(
    viewModel: HomeViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val history = uiState.balanceHistory

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Balance Analytics",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = HomeBackground
                )
            )
        },
        containerColor = HomeBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = BlueGradientStart.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(24.dp),
                                tint = BlueGradientStart
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Current Balance", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = "$${"%.2f".format(uiState.netBalance)}",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    if (history.size < 2) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Not enough data to show trend", color = Color.Gray)
                        }
                    } else {
                        BalanceLineChart(
                            history = history,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Trend History",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (history.isEmpty()) {
                Text("No transaction history found.", color = Color.Gray)
            } else {
                history.reversed().forEach { data ->
                    HistoryItem(data)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(data: BalanceHistoryData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = data.date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = data.date.format(DateTimeFormatter.ofPattern("EEEE")),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
            }
            Text(
                text = "$${"%.2f".format(data.balance)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (data.balance >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
            )
        }
    }
}

@Composable
fun BalanceLineChart(
    history: List<BalanceHistoryData>,
    modifier: Modifier = Modifier
) {
    val minB = history.minOf { it.balance }
    val maxB = history.maxOf { it.balance }
    
    // Add buffer to avoid chart hitting the edges
    val rangeRaw = (maxB - minB).coerceAtLeast(1.0)
    val minBalance = minB - (rangeRaw * 0.15)
    val maxBalance = maxB + (rangeRaw * 0.15)
    val range = (maxBalance - minBalance).coerceAtLeast(1.0)
    
    val chartColor = BlueGradientStart

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        
        if (width <= 0 || height <= 0 || history.size < 2) return@Canvas

        val spacing = width / (history.size - 1)

        val points = history.mapIndexed { index, data ->
            val x = index * spacing
            val normalizedY = ((data.balance - minBalance) / range).toFloat()
            val y = height - (normalizedY * height)
            Offset(x, y)
        }

        // Draw Area Gradient
        val fillPath = Path().apply {
            if (points.isNotEmpty()) {
                moveTo(points.first().x, height)
                points.forEach { lineTo(it.x, it.y) }
                lineTo(points.last().x, height)
                close()
            }
        }
        
        if (points.isNotEmpty()) {
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(chartColor.copy(alpha = 0.3f), Color.Transparent)
                )
            )

            // Draw Line
            val strokePath = Path().apply {
                moveTo(points.first().x, points.first().y)
                points.forEach { lineTo(it.x, it.y) }
            }
            drawPath(
                path = strokePath,
                color = chartColor,
                style = Stroke(width = 3.dp.toPx())
            )

            // Draw Points
            points.forEach { center ->
                drawCircle(color = Color.White, radius = 4.dp.toPx(), center = center)
                drawCircle(
                    color = chartColor, 
                    radius = 4.dp.toPx(), 
                    center = center, 
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
    }
}
