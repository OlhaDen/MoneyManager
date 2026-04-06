package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ExpenseCard(
    id: Int,
    title: String,
    amount: String,
    date: String,
    isIncome: Boolean,
    category: String,
    onDeleteClick: (Int) -> Unit
) {
    val bgColor = if (isIncome) Color(0xFFEAF8EF) else Color.White
    val amountColor = if (isIncome) Color(0xFF1FBF75) else Color(0xFF111111)
    val icon = categoryToIcon(category)

    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgColor)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    text = date,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = amount,
                color = amountColor,
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(onClick = { onDeleteClick(id) }) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = Color.Gray
                )
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