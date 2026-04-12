package com.example.myapplication.ui.screens.history

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.screens.home.HomeViewModel
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
            TopAppBar(
                title = { Text("Balance Over Time") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = HomeBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(
                text = "Net Balance Trend",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Total Balance: $${"%.2f".format(uiState.netBalance)}",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (history.size < 2) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Add more transactions to see the trend")
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    BalanceLineChart(
                        history = history,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp, bottom = 40.dp, start = 52.dp, end = 20.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text("History Points", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                history.reversed().take(10).forEach { data ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(data.date.toString(), color = Color.Gray)
                        Text(
                            text = "$${"%.2f".format(data.balance)}",
                            fontWeight = FontWeight.Bold,
                            color = if (data.balance >= 0) Color(0xFF4CAF50) else Color(0xFFE91E63)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BalanceLineChart(
    history: List<com.example.myapplication.ui.screens.home.BalanceHistoryData>,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(
        color = Color.Gray,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium
    )

    val minBalance = history.minOf { it.balance }.let { if (it > 0) 0.0 else it }
    val maxBalance = history.maxOf { it.balance }.let { if (it < 0) 0.0 else it }
    val range = (maxBalance - minBalance).coerceAtLeast(1.0)
    
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM")

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        
        if (width <= 0 || height <= 0) return@Canvas

        val spacing = width / (history.size - 1)

        // Draw Y-axis labels
        val yLabels = listOf(maxBalance, (maxBalance + minBalance) / 2, minBalance)
        yLabels.forEach { labelValue ->
            val normalizedY = (labelValue - minBalance) / range
            val y = height - (normalizedY.toFloat() * height)
            
            val amountText = "$${labelValue.toInt()}"
            val textLayoutResult = textMeasurer.measure(amountText, labelStyle)
            
            drawText(
                textMeasurer = textMeasurer,
                text = amountText,
                style = labelStyle,
                topLeft = Offset(-textLayoutResult.size.width.toFloat() - 8.dp.toPx(), y - textLayoutResult.size.height / 2f)
            )
            
            // Grid line
            drawLine(
                color = Color.LightGray.copy(alpha = 0.5f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Draw Path
        val path = Path()
        history.forEachIndexed { index, data ->
            val x = index * spacing
            val normalizedY = (data.balance - minBalance) / range
            val y = height - (normalizedY.toFloat() * height)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
            
            // Draw X-axis labels (Date)
            if (index == 0 || index == history.size - 1 || (history.size > 5 && index == history.size / 2)) {
                val dateText = data.date.format(dateFormatter)
                val textLayoutResult = textMeasurer.measure(dateText, labelStyle)
                drawText(
                    textMeasurer = textMeasurer,
                    text = dateText,
                    style = labelStyle,
                    topLeft = Offset(x - textLayoutResult.size.width / 2f, height + 8.dp.toPx())
                )
            }

            drawCircle(
                color = Color(0xFF2196F3),
                radius = 3.dp.toPx(),
                center = Offset(x, y)
            )
        }

        drawPath(
            path = path,
            color = Color(0xFF2196F3),
            style = Stroke(width = 2.dp.toPx())
        )
    }
}
