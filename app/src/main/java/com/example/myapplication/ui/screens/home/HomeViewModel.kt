package com.example.myapplication.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.TransactionEntity
import com.example.myapplication.data.repository.TransactionRepository
import com.example.myapplication.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class HomeUiState(
    val transactions: List<TransactionEntity> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val netBalance: Double = 0.0
)

class HomeViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeTransactions()
    }

    private fun observeTransactions() {
        viewModelScope.launch {
            transactionRepository.getAllTransactions().collectLatest { transactions ->
                val income = transactions
                    .filter { it.type == TransactionType.INCOME.name }
                    .sumOf { it.amount }

                val expenses = transactions
                    .filter { it.type == TransactionType.EXPENSE.name }
                    .sumOf { it.amount }

                _uiState.value = HomeUiState(
                    transactions = transactions,
                    totalIncome = income,
                    totalExpenses = expenses,
                    netBalance = income - expenses
                )
            }
        }
    }

    fun addTransaction(
        amount: Double,
        category: String,
        description: String,
        date: String,
        type: TransactionType
    ) {
        viewModelScope.launch {
            transactionRepository.insertTransaction(
                TransactionEntity(
                    amount = amount,
                    category = category,
                    description = description,
                    date = date,
                    type = type.name
                )
            )
        }
    }

    fun deleteTransaction(id: Int) {
        viewModelScope.launch {
            transactionRepository.deleteTransactionById(id)
        }
    }
}

class HomeViewModelFactory(
    private val transactionRepository: TransactionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(transactionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}