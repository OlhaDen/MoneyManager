package com.example.myapplication.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.domain.model.TransactionType
import com.example.myapplication.ui.components.ExpenseCard
import com.example.myapplication.ui.screens.home.HomeFilterPeriod
import com.example.myapplication.ui.screens.home.HomeViewModel
import com.example.myapplication.ui.theme.HomeBackground
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HistoryScreen(
    viewModel: HomeViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = uiState.filteredTransactions.filter {
        it.description.contains(searchQuery, ignoreCase = true) ||
        it.category.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction History") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = HomeBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search transactions...") },
                        leadingIcon = { Icon(Icons.Outlined.Search, null) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        HomeFilterPeriod.values().forEach { period ->
                            FilterChip(
                                selected = uiState.selectedPeriod == period,
                                onClick = { viewModel.setFilterPeriod(period) },
                                label = { 
                                    Text(period.name.lowercase().replaceFirstChar { it.uppercase() }) 
                                }
                            )
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList) { transaction ->
                    val isIncome = transaction.type == TransactionType.INCOME.name
                    val formattedAmount = if (isIncome) {
                        "+$${"%.2f".format(transaction.amount)}"
                    } else {
                        "-$${"%.2f".format(transaction.amount)}"
                    }

                    ExpenseCard(
                        id = transaction.id,
                        title = transaction.description,
                        amount = formattedAmount,
                        date = transaction.date,
                        isIncome = isIncome,
                        category = transaction.category,
                        onDeleteClick = { id ->
                            viewModel.deleteTransaction(id)
                        }
                    )
                }
            }
        }
    }
}
