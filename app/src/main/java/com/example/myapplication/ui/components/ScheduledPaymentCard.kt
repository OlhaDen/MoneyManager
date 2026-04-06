package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ScheduledPaymentCard(
    id: Int,
    title: String,
    amount: String,
    dueDate: String,
    recurring: String,
    isIncome: Boolean,
    isPaid: Boolean,
    category: String,
    onMarkPaidClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    val bgColor = when {
        isPaid -> Color(0xFFF3F4F6)
        isIncome -> Color(0xFFEAF8EF)
        else -> Color.White
    }

    val amountColor = if (isIncome) Color(0xFF1FBF75) else Color(0xFF111111)
    val icon = categoryToIcon(category)

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgColor)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(Color(0xFFF8F4E8), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFFFFB300)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Due: $dueDate",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                    if (recurring != "NONE") {
                        Text(
                            text = recurring,
                            color = Color(0xFF7A64FF),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Text(
                    text = amount,
                    color = amountColor,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!isPaid) {
                    Button(
                        onClick = { onMarkPaidClick(id) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Outlined.CheckCircle, null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Mark as Paid")
                    }
                }

                OutlinedButton(
                    onClick = { onDeleteClick(id) },
                    modifier = Modifier.weight(if (isPaid) 1f else 0.8f)
                ) {
                    Icon(Icons.Outlined.Delete, null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Delete")
                }
            }
        }
    }
}

private fun categoryToIcon(category: String): ImageVector {
    return when (category.lowercase()) {
        "food" -> Icons.Outlined.Fastfood
        "shopping" -> Icons.Outlined.ShoppingBag
        else -> Icons.Outlined.CardGiftcard
    }
}