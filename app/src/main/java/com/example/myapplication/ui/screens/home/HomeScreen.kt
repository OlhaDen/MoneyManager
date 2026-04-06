package com.example.myapplication.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.domain.model.TransactionType
import com.example.myapplication.ui.components.AddTransactionDialog
import com.example.myapplication.ui.components.EmptyStateCard
import com.example.myapplication.ui.components.ExpenseCard
import com.example.myapplication.ui.components.GradientHeader
import com.example.myapplication.ui.theme.BlueGradientEnd
import com.example.myapplication.ui.theme.BlueGradientStart
import com.example.myapplication.ui.theme.FabColor
import com.example.myapplication.ui.theme.HomeBackground

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onScheduledClick: () -> Unit,
    onInfoClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            GradientHeader(
                title = "Expense Tracker",
                gradientColors = listOf(BlueGradientStart, BlueGradientEnd),
                balance = "$${"%.2f".format(uiState.netBalance)}",
                subtitle = "All Time",
                income = "+$${"%.2f".format(uiState.totalIncome)}",
                expenses = "-$${"%.2f".format(uiState.totalExpenses)}",
                icon = Icons.Outlined.TrendingDown,
                topContent = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Expense Tracker",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Row {
                            IconButton(onClick = onScheduledClick) {
                                Icon(Icons.Outlined.Schedule, null, tint = Color.White)
                            }
                            IconButton(onClick = onInfoClick) {
                                Icon(Icons.Outlined.Info, null, tint = Color.White)
                            }
                            IconButton(onClick = onLogoutClick) {
                                Icon(Icons.Outlined.Logout, null, tint = Color.White)
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "All Time",
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.transactions.isEmpty()) {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    EmptyStateCard(
                        title = "No expenses yet",
                        subtitle = "Tap the + button to add your first expense",
                        icon = Icons.Outlined.Wallet
                    )
                }
            } else {
                Text(
                    text = "${uiState.transactions.size} transactions",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    uiState.transactions.forEach { transaction ->
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

            Spacer(modifier = Modifier.height(100.dp))
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = FabColor,
            contentColor = Color.White
        ) {
            Icon(Icons.Outlined.Add, contentDescription = null)
        }

        AddTransactionDialog(
            openDialog = showDialog,
            onDismiss = { showDialog = false },
            onAddTransaction = { amount, category, description, date, type ->
                viewModel.addTransaction(
                    amount = amount,
                    category = category,
                    description = description,
                    date = date,
                    type = type
                )
            }
        )
    }
}