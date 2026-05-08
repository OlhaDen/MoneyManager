package com.example.myapplication.ui.screens.scheduled

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserSessionManager
import com.example.myapplication.data.local.entity.ScheduledPaymentEntity
import com.example.myapplication.data.local.entity.TransactionEntity
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.ScheduledPaymentRepository
import com.example.myapplication.data.repository.TransactionRepository
import com.example.myapplication.domain.model.RecurringType
import com.example.myapplication.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ScheduledPaymentsUiState(
    val allPayments: List<ScheduledPaymentEntity> = emptyList(),
    val upcomingPayments: List<ScheduledPaymentEntity> = emptyList(),
    val paidPayments: List<ScheduledPaymentEntity> = emptyList(),
    val globalNetBalance: Double = 0.0
)

class ScheduledPaymentsViewModel(
    private val scheduledPaymentRepository: ScheduledPaymentRepository,
    private val transactionRepository: TransactionRepository,
    private val authRepository: AuthRepository,
    private val userSessionManager: UserSessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduledPaymentsUiState())
    val uiState: StateFlow<ScheduledPaymentsUiState> = _uiState.asStateFlow()

    private var currentUserId: Int? = null

    fun refreshUserSession() {
        val userId = userSessionManager.getUserId()
        Log.d("ScheduledVM", "refreshUserSession: userId=$userId (previous=$currentUserId)")
        if (userId != -1 && userId != currentUserId) {
            currentUserId = userId
            observePayments(userId)
        } else if (userId == -1) {
            currentUserId = null
            _uiState.value = ScheduledPaymentsUiState()
        }
    }

    private fun observePayments(userId: Int) {
        Log.d("ScheduledVM", "observePayments for userId=$userId")
        viewModelScope.launch {
            scheduledPaymentRepository.getAllPayments(userId).collectLatest { payments ->
                Log.d("ScheduledVM", "Received ${payments.size} payments for userId=$userId")
                _uiState.update {
                    it.copy(
                        allPayments = payments,
                        upcomingPayments = payments.filter { !it.isPaid },
                        paidPayments = payments.filter { it.isPaid }
                    )
                }
            }
        }

        viewModelScope.launch {
            transactionRepository.getAllTransactions(userId).collectLatest { transactions ->
                val income = transactions.filter { it.type == TransactionType.INCOME.name }.sumOf { it.amount }
                val expenses = transactions.filter { it.type == TransactionType.EXPENSE.name }.sumOf { it.amount }
                _uiState.update {
                    it.copy(
                        globalNetBalance = income - expenses
                    )
                }
            }
        }
    }

    fun addPayment(
        amount: Double,
        category: String,
        description: String,
        dueDate: String,
        type: TransactionType,
        recurring: RecurringType
    ) {
        val userId = currentUserId ?: userSessionManager.getUserId().takeIf { it != -1 } ?: run {
            Log.e("ScheduledVM", "addPayment failed: userId is null")
            return
        }
        Log.d("ScheduledVM", "addPayment: userId=$userId, amount=$amount")
        viewModelScope.launch {
            scheduledPaymentRepository.insertPayment(
                ScheduledPaymentEntity(
                    userId = userId,
                    amount = amount,
                    category = category,
                    description = description,
                    dueDate = dueDate,
                    type = type.name,
                    recurring = recurring.name,
                    isPaid = false
                )
            )
        }
    }

    fun deletePayment(id: Int) {
        viewModelScope.launch {
            scheduledPaymentRepository.deletePaymentById(id)
        }
    }

    fun markAsPaid(id: Int) {
        viewModelScope.launch {
            val payment = scheduledPaymentRepository.getPaymentById(id) ?: return@launch

            if (payment.isPaid) return@launch

            scheduledPaymentRepository.updatePayment(
                payment.copy(isPaid = true)
            )

            transactionRepository.insertTransaction(
                TransactionEntity(
                    userId = payment.userId,
                    amount = payment.amount,
                    category = payment.category,
                    description = payment.description,
                    date = payment.dueDate,
                    type = payment.type
                )
            )
        }
    }
}

class ScheduledPaymentsViewModelFactory(
    private val scheduledPaymentRepository: ScheduledPaymentRepository,
    private val transactionRepository: TransactionRepository,
    private val authRepository: AuthRepository,
    private val userSessionManager: UserSessionManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduledPaymentsViewModel::class.java)) {
            return ScheduledPaymentsViewModel(
                scheduledPaymentRepository,
                transactionRepository,
                authRepository,
                userSessionManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
