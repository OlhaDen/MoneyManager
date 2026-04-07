package com.example.myapplication.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.screens.home.ChartCategoryData

@Composable
fun ExpenseChartCard(
    data: List<ChartCategoryData>
) {
    if (data.isEmpty()) return

    val colors = listOf(
        Color(0xFFFF6B6B),
        Color(0xFFF4D35E),
        Color(0xFF4ECDC4),
        Color(0xFF7A77FF),
        Color(0xFF45B7D1),
        Color(0xFFFF8C42),
        Color(0xFF8BD17C)
    )

    val total = data.sumOf { it.amount }.toFloat()
    if (total <= 0f) return

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Spending by Category",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier.size(220.dp)
                ) {
                    var startAngle = -90f

                    data.forEachIndexed { index, item ->
                        val sweepAngle = (item.amount.toFloat() / total) * 360f
                        drawArc(
                            color = colors[index % colors.size],
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = true,
                            size = Size(size.width, size.height)
                        )
                        startAngle += sweepAngle
                    }

                    drawCircle(
                        color = Color.White,
                        radius = size.minDimension * 0.28f
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                data.forEachIndexed { index, item ->
                    val percent = ((item.amount / total) * 100).toInt()

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .fillMaxSize()
                        ) {
                            Canvas(modifier = Modifier.size(12.dp)) {
                                drawCircle(colors[index % colors.size])
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = item.category,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "$${"%.2f".format(item.amount)}",
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "$percent%",
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}