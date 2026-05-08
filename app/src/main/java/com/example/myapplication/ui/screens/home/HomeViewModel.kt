package com.example.myapplication.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserSessionManager
import com.example.myapplication.data.local.entity.TransactionEntity
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.TransactionRepository
import com.example.myapplication.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class HomeFilterPeriod {
    DAY, WEEK, MONTH, YEAR, ALL
}

data class ChartCategoryData(
    val category: String,
    val amount: Double
)

data class BalanceHistoryData(
    val date: LocalDate,
    val balance: Double
)

data class HomeUiState(
    val allTransactions: List<TransactionEntity> = emptyList(),
    val filteredTransactions: List<TransactionEntity> = emptyList(),
    val recentTransactions: List<TransactionEntity> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val netBalance: Double = 0.0,
    val selectedPeriod: HomeFilterPeriod = HomeFilterPeriod.ALL,
    val chartData: List<ChartCategoryData> = emptyList(),
    val balanceHistory: List<BalanceHistoryData> = emptyList()
)

class HomeViewModel(
    private val transactionRepository: TransactionRepository,
    private val authRepository: AuthRepository,
    private val userSessionManager: UserSessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var currentUserId: Int? = null

    fun refreshUserSession() {
        val userId = userSessionManager.getUserId()
        Log.d("HomeViewModel", "refreshUserSession: userId=$userId (previous=$currentUserId)")
        if (userId != -1 && userId != currentUserId) {
            currentUserId = userId
            observeTransactions(userId)
        } else if (userId == -1) {
            currentUserId = null
            _uiState.value = HomeUiState() // Clear state on logout
        }
    }

    private fun observeTransactions(userId: Int) {
        Log.d("HomeViewModel", "observeTransactions for userId=$userId")
        viewModelScope.launch {
            transactionRepository.getAllTransactions(userId).collectLatest { transactions ->
                Log.d("HomeViewModel", "Received ${transactions.size} transactions for userId=$userId")
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
        
        // Sort all transactions by date descending
        val sortedTransactions = allTransactions.sortedByDescending { it.date }
        val recent = sortedTransactions.take(5)

        val filtered = sortedTransactions.filter { transaction ->
            val transactionDate = runCatching { LocalDate.parse(transaction.date) }.getOrNull()
                ?: return@filter false

            when (period) {
                HomeFilterPeriod.DAY -> transactionDate.isEqual(today)
                HomeFilterPeriod.WEEK -> !transactionDate.isBefore(today.minusDays(6))
                HomeFilterPeriod.MONTH -> !transactionDate.isBefore(today.minusDays(29))
                HomeFilterPeriod.YEAR -> !transactionDate.isBefore(today.minusYears(1))
                HomeFilterPeriod.ALL -> true
            }
        }

        val totalIncomeGlobal = allTransactions
            .filter { it.type == TransactionType.INCOME.name }
            .sumOf { it.amount }

        val totalExpensesGlobal = allTransactions
            .filter { it.type == TransactionType.EXPENSE.name }
            .sumOf { it.amount }

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

        // Calculate balance history
        val balanceHistory = allTransactions
            .mapNotNull { transaction ->
                val date = runCatching { LocalDate.parse(transaction.date) }.getOrNull() ?: return@mapNotNull null
                val amount = if (transaction.type == TransactionType.INCOME.name) transaction.amount else -transaction.amount
                date to amount
            }
            .groupBy { it.first }
            .mapValues { entry -> entry.value.sumOf { it.second } }
            .toSortedMap()
            .let { sortedDailySums ->
                var currentBalance = 0.0
                sortedDailySums.map { (date, dailySum) ->
                    currentBalance += dailySum
                    BalanceHistoryData(date, currentBalance)
                }
            }

        _uiState.update { 
            it.copy(
                allTransactions = sortedTransactions,
                filteredTransactions = filtered,
                recentTransactions = recent,
                totalIncome = income,
                totalExpenses = expenses,
                netBalance = totalIncomeGlobal - totalExpensesGlobal,
                selectedPeriod = period,
                chartData = chartData,
                balanceHistory = balanceHistory
            )
        }
    }

    fun addTransaction(
        amount: Double,
        category: String,
        description: String,
        date: String,
        type: TransactionType
    ) {
        val userId = currentUserId ?: userSessionManager.getUserId().takeIf { it != -1 } ?: run {
            Log.e("HomeViewModel", "addTransaction failed: userId is null")
            return
        }
        Log.d("HomeViewModel", "addTransaction: userId=$userId, amount=$amount, category=$category")
        viewModelScope.launch {
            transactionRepository.insertTransaction(
                TransactionEntity(
                    userId = userId,
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
    private val transactionRepository: TransactionRepository,
    private val authRepository: AuthRepository,
    private val userSessionManager: UserSessionManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                transactionRepository,
                authRepository,
                userSessionManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
