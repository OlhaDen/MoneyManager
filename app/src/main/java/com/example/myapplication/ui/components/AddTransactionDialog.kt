package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.domain.model.TransactionType
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    openDialog: Boolean,
    onDismiss: () -> Unit,
    onAddTransaction: (
        amount: Double,
        category: String,
        description: String,
        date: String,
        type: TransactionType
    ) -> Unit
) {
    if (!openDialog) return

    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Food") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now().toString()) }

    val expenseCategories = listOf(
        "Food", "Transport", "Shopping", "Bills", "Entertainment", "Health", "Other"
    )
    val incomeCategories = listOf(
        "Salary", "Freelance", "Investment", "Gift", "Refund", "Other"
    )

    val categories = if (selectedType == TransactionType.EXPENSE) {
        expenseCategories
    } else {
        incomeCategories
    }

    LaunchedEffect(selectedType) {
        category = if (selectedType == TransactionType.EXPENSE) "Food" else "Salary"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Text("Add Transaction")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectedType == TransactionType.EXPENSE,
                        onClick = { selectedType = TransactionType.EXPENSE },
                        label = { Text("Expense") }
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

                CategoryDropdown(
                    selectedCategory = category,
                    categories = categories,
                    onCategorySelected = { category = it }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    placeholder = { Text("Optional") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date") },
                    placeholder = { Text("YYYY-MM-DD") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                Button(
                    onClick = {
                        val parsedAmount = amount.toDoubleOrNull()
                        if (parsedAmount != null && parsedAmount > 0.0) {
                            onAddTransaction(
                                parsedAmount,
                                category,
                                if (description.isBlank()) category else description,
                                date,
                                selectedType
                            )
                            onDismiss()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (selectedType == TransactionType.EXPENSE) {
                            "Add Expense"
                        } else {
                            "Add Income"
                        }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(
    selectedCategory: String,
    categories: List<String>,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onCategorySelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}