package com.example.myapplication.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.*
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
import com.example.myapplication.ui.components.ExpenseChartCard
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
    onLogoutClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onBalanceChartClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.refreshUserSession()
    }
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
                income = "+$${"%.2f".format(uiState.totalIncome)}",
                expenses = "-$${"%.2f".format(uiState.totalExpenses)}",
                icon = Icons.Outlined.Wallet,
                onChartClick = onBalanceChartClick,
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
                            var showMenu by remember { mutableStateOf(false) }
                            
                            IconButton(onClick = { showMenu = true }) {
                                Icon(Icons.Default.MoreVert, "Menu", tint = Color.White)
                            }
                            
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Scheduled Payments") },
                                    onClick = {
                                        showMenu = false
                                        onScheduledClick()
                                    },
                                    leadingIcon = { Icon(Icons.Outlined.Schedule, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("App Info") },
                                    onClick = {
                                        showMenu = false
                                        onInfoClick()
                                    },
                                    leadingIcon = { Icon(Icons.Outlined.Info, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Transaction History") },
                                    onClick = {
                                        showMenu = false
                                        onHistoryClick()
                                    },
                                    leadingIcon = { Icon(Icons.Outlined.History, null) }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Logout") },
                                    onClick = {
                                        showMenu = false
                                        onLogoutClick()
                                    },
                                    leadingIcon = { Icon(Icons.Outlined.Logout, null) }
                                )
                            }
                        }
                    }
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            PeriodFilterCard(
                selectedPeriod = uiState.selectedPeriod,
                onPeriodSelected = { viewModel.setFilterPeriod(it) }
            )

            if (uiState.chartData.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    ExpenseChartCard(data = uiState.chartData)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.recentTransactions.isEmpty()) {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    EmptyStateCard(
                        title = "No expenses yet",
                        subtitle = "Add your first expense to see it here",
                        icon = Icons.Outlined.Wallet
                    )
                }
            } else {
                Text(
                    text = "Recent transactions",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    uiState.recentTransactions.forEach { transaction ->
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PeriodFilterCard(
    selectedPeriod: HomeFilterPeriod,
    onPeriodSelected: (HomeFilterPeriod) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Filter period",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                FilterChip(
                    selected = selectedPeriod == HomeFilterPeriod.DAY,
                    onClick = { onPeriodSelected(HomeFilterPeriod.DAY) },
                    label = { Text("Day") }
                )
                FilterChip(
                    selected = selectedPeriod == HomeFilterPeriod.WEEK,
                    onClick = { onPeriodSelected(HomeFilterPeriod.WEEK) },
                    label = { Text("Week") }
                )
                FilterChip(
                    selected = selectedPeriod == HomeFilterPeriod.MONTH,
                    onClick = { onPeriodSelected(HomeFilterPeriod.MONTH) },
                    label = { Text("Month") }
                )
                FilterChip(
                    selected = selectedPeriod == HomeFilterPeriod.YEAR,
                    onClick = { onPeriodSelected(HomeFilterPeriod.YEAR) },
                    label = { Text("Year") }
                )
                FilterChip(
                    selected = selectedPeriod == HomeFilterPeriod.ALL,
                    onClick = { onPeriodSelected(HomeFilterPeriod.ALL) },
                    label = { Text("All") }
                )
            }
        }
    }
}
