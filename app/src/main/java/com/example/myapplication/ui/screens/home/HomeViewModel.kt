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
import java.time.LocalDate

enum class HomeFilterPeriod {
    DAY, WEEK, MONTH, ALL
}

data class ChartCategoryData(
    val category: String,
    val amount: Double
)

data class HomeUiState(
    val allTransactions: List<TransactionEntity> = emptyList(),
    val filteredTransactions: List<TransactionEntity> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val netBalance: Double = 0.0,
    val selectedPeriod: HomeFilterPeriod = HomeFilterPeriod.ALL,
    val chartData: List<ChartCategoryData> = emptyList()
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
                updateUiWithFilter(
                    allTransactions = transactions,
                    period = _uiState.value.selectedPeriod
                )
            }
        }
    }

    fun setFilterPeriod(period: HomeFilterPeriod) {
        updateUiWithFilter(
            allTransactions = _uiState.value.allTransactions,
            period = period
        )
    }

    private fun updateUiWithFilter(
        allTransactions: List<TransactionEntity>,
        period: HomeFilterPeriod
    ) {
        val today = LocalDate.now()

        val filtered = allTransactions.filter { transaction ->
            val transactionDate = runCatching { LocalDate.parse(transaction.date) }.getOrNull()
                ?: return@filter false

            when (period) {
                HomeFilterPeriod.DAY -> transactionDate.isEqual(today)
                HomeFilterPeriod.WEEK -> !transactionDate.isBefore(today.minusDays(6))
                HomeFilterPeriod.MONTH -> !transactionDate.isBefore(today.minusDays(29))
                HomeFilterPeriod.ALL -> true
            }
        }

        val income = filtered
            .filter { it.type == TransactionType.INCOME.name }
            .sumOf { it.amount }

        val expenses = filtered
            .filter { it.type == TransactionType.EXPENSE.name }
            .sumOf { it.amount }

        val chartData = filtered
            .filter { it.type == TransactionType.EXPENSE.name }
            .groupBy { it.category }
            .map { (category, items) ->
                ChartCategoryData(
                    category = category,
                    amount = items.sumOf { it.amount }
                )
            }
            .sortedByDescending { it.amount }

        _uiState.value = HomeUiState(
            allTransactions = allTransactions,
            filteredTransactions = filtered,
            totalIncome = income,
            totalExpenses = expenses,
            netBalance = income - expenses,
            selectedPeriod = period,
            chartData = chartData
        )
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