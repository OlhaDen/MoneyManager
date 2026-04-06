package com.example.myapplication.ui.screens.scheduled

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.domain.model.TransactionType
import com.example.myapplication.ui.components.AddScheduledPaymentDialog
import com.example.myapplication.ui.components.EmptyStateCard
import com.example.myapplication.ui.components.GradientHeader
import com.example.myapplication.ui.components.ScheduledPaymentCard
import com.example.myapplication.ui.theme.FabColor
import com.example.myapplication.ui.theme.PinkEnd
import com.example.myapplication.ui.theme.PinkStart
import com.example.myapplication.ui.theme.ScheduledBackground

@Composable
fun ScheduledPaymentsScreen(
    viewModel: ScheduledPaymentsViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf("Upcoming") }
    var showDialog by remember { mutableStateOf(false) }

    val payments = if (selectedTab == "Upcoming") {
        uiState.upcomingPayments
    } else {
        uiState.paidPayments
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScheduledBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            GradientHeader(
                title = "Scheduled Payments",
                gradientColors = listOf(PinkStart, PinkEnd),
                balance = uiState.upcomingPayments.size.toString(),
                subtitle = "Upcoming Payments",
                income = "",
                expenses = "",
                icon = Icons.Outlined.Schedule,
                topContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Outlined.ArrowBack, null, tint = Color.White)
                        }
                        Text(
                            text = "Scheduled Payments",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                ) {
                    Button(
                        onClick = { selectedTab = "Upcoming" },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Upcoming")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = { selectedTab = "Paid" },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Paid")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (payments.isEmpty()) {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    EmptyStateCard(
                        title = if (selectedTab == "Upcoming") {
                            "No upcoming payments"
                        } else {
                            "No paid payments"
                        },
                        subtitle = if (selectedTab == "Upcoming") {
                            "Tap the + button to add a scheduled payment"
                        } else {
                            "Paid payments will appear here"
                        },
                        icon = Icons.Outlined.CalendarToday
                    )
                }
            } else {
                Text(
                    text = "${payments.size} payments",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    payments.forEach { payment ->
                        val isIncome = payment.type == TransactionType.INCOME.name
                        val formattedAmount = if (isIncome) {
                            "+$${"%.2f".format(payment.amount)}"
                        } else {
                            "-$${"%.2f".format(payment.amount)}"
                        }

                        ScheduledPaymentCard(
                            id = payment.id,
                            title = payment.description,
                            amount = formattedAmount,
                            dueDate = payment.dueDate,
                            recurring = payment.recurring,
                            isIncome = isIncome,
                            isPaid = payment.isPaid,
                            category = payment.category,
                            onMarkPaidClick = { id ->
                                viewModel.markAsPaid(id)
                            },
                            onDeleteClick = { id ->
                                viewModel.deletePayment(id)
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

        AddScheduledPaymentDialog(
            openDialog = showDialog,
            onDismiss = { showDialog = false },
            onAddPayment = { amount, category, description, dueDate, type, recurring ->
                viewModel.addPayment(
                    amount = amount,
                    category = category,
                    description = description,
                    dueDate = dueDate,
                    type = type,
                    recurring = recurring
                )
            }
        )
    }
}