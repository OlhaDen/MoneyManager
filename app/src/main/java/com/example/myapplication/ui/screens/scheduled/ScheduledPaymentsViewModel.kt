package com.example.myapplication.ui.screens.scheduled

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.ScheduledPaymentEntity
import com.example.myapplication.data.local.entity.TransactionEntity
import com.example.myapplication.data.repository.ScheduledPaymentRepository
import com.example.myapplication.data.repository.TransactionRepository
import com.example.myapplication.domain.model.RecurringType
import com.example.myapplication.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class ScheduledPaymentsUiState(
    val allPayments: List<ScheduledPaymentEntity> = emptyList(),
    val upcomingPayments: List<ScheduledPaymentEntity> = emptyList(),
    val paidPayments: List<ScheduledPaymentEntity> = emptyList()
)

class ScheduledPaymentsViewModel(
    private val scheduledPaymentRepository: ScheduledPaymentRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduledPaymentsUiState())
    val uiState: StateFlow<ScheduledPaymentsUiState> = _uiState.asStateFlow()

    init {
        observePayments()
    }

    private fun observePayments() {
        viewModelScope.launch {
            scheduledPaymentRepository.getAllPayments().collectLatest { payments ->
                _uiState.value = ScheduledPaymentsUiState(
                    allPayments = payments,
                    upcomingPayments = payments.filter { !it.isPaid },
                    paidPayments = payments.filter { it.isPaid }
                )
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
        viewModelScope.launch {
            scheduledPaymentRepository.insertPayment(
                ScheduledPaymentEntity(
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
    private val transactionRepository: TransactionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduledPaymentsViewModel::class.java)) {
            return ScheduledPaymentsViewModel(
                scheduledPaymentRepository,
                transactionRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}