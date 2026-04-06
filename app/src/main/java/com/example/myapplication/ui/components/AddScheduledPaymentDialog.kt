package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.domain.model.RecurringType
import com.example.myapplication.domain.model.TransactionType
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduledPaymentDialog(
    openDialog: Boolean,
    onDismiss: () -> Unit,
    onAddPayment: (
        amount: Double,
        category: String,
        description: String,
        dueDate: String,
        type: TransactionType,
        recurring: RecurringType
    ) -> Unit
) {
    if (!openDialog) return

    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Bills") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf(LocalDate.now().toString()) }
    var recurring by remember { mutableStateOf(RecurringType.NONE) }

    val expenseCategories = listOf(
        "Food", "Transport", "Shopping", "Bills", "Entertainment", "Health", "Other"
    )
    val incomeCategories = listOf(
        "Salary", "Freelance", "Investment", "Gift", "Refund", "Other"
    )

    val recurringOptions = listOf(
        RecurringType.NONE,
        RecurringType.DAILY,
        RecurringType.WEEKLY,
        RecurringType.MONTHLY,
        RecurringType.YEARLY
    )

    val categories = if (selectedType == TransactionType.EXPENSE) {
        expenseCategories
    } else {
        incomeCategories
    }

    LaunchedEffect(selectedType) {
        category = if (selectedType == TransactionType.EXPENSE) "Bills" else "Salary"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Text("Add Scheduled Payment")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectedType == TransactionType.EXPENSE,
                        onClick = { selectedType = TransactionType.EXPENSE },
                        label = { Text("Payment") }
                    )
                    FilterChip(
                        selected = selectedType == TransactionType.INCOME,
                        onClick = { selectedType = TransactionType.INCOME },
                        label = { Text("Income") }
                    )
                }

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    placeholder = { Text("0.00") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                CategoryDropdownSimple(
                    label = "Category",
                    selectedValue = category,
                    values = categories,
                    onValueSelected = { category = it }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    placeholder = { Text("e.g. Rent, Electric bill") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date") },
                    placeholder = { Text("YYYY-MM-DD") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                CategoryDropdownSimple(
                    label = "Recurring",
                    selectedValue = recurring.name,
                    values = recurringOptions.map { it.name },
                    onValueSelected = { recurring = RecurringType.valueOf(it) }
                )

                Button(
                    onClick = {
                        val parsedAmount = amount.toDoubleOrNull()
                        if (parsedAmount != null && parsedAmount > 0.0) {
                            onAddPayment(
                                parsedAmount,
                                category,
                                if (description.isBlank()) category else description,
                                dueDate,
                                selectedType,
                                recurring
                            )
                            onDismiss()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Scheduled Payment")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdownSimple(
    label: String,
    selectedValue: String,
    values: List<String>,
    onValueSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(16.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            values.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onValueSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}