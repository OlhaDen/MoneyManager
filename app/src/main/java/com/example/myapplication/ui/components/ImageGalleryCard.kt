package com.example.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

data class GalleryImage(
    val resId: Int,
    val label: String
)

@Composable
fun ImageGalleryCard() {
    val images = listOf(
        GalleryImage(R.drawable.finance_1, "Finance 1"),
        GalleryImage(R.drawable.finance_2, "Finance 2"),
        GalleryImage(R.drawable.finance_3, "Finance 3"),
        GalleryImage(R.drawable.finance_4, "Finance 4"),
        GalleryImage(R.drawable.finance_5, "Finance 5"),
        GalleryImage(R.drawable.finance_6, "Finance 6"),
        GalleryImage(R.drawable.finance_7, "Finance 7"),
        GalleryImage(R.drawable.finance_8, "Finance 8"),
        GalleryImage(R.drawable.finance_9, "Finance 9"),
        GalleryImage(R.drawable.finance_10, "Finance 10"),
        GalleryImage(R.drawable.finance_11, "Finance 11"),
        GalleryImage(R.drawable.finance_12, "Finance 12"),
        GalleryImage(R.drawable.finance_13, "Finance 13"),
        GalleryImage(R.drawable.finance_14, "Finance 14"),
        GalleryImage(R.drawable.finance_15, "Finance 15")
    )

    Card {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "App Gallery",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sekcja obrazów użytych w projekcie.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items(images) { item ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = item.resId),
                            contentDescription = item.label,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFF4F6FA)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}