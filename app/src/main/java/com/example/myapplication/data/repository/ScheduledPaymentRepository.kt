package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.ScheduledPaymentDao
import com.example.myapplication.data.local.entity.ScheduledPaymentEntity
import kotlinx.coroutines.flow.Flow

class ScheduledPaymentRepository(
    private val scheduledPaymentDao: ScheduledPaymentDao
) {
    fun getAllPayments(userId: Int): Flow<List<ScheduledPaymentEntity>> {
        return scheduledPaymentDao.getAllPayments(userId)
    }

    suspend fun insertPayment(payment: ScheduledPaymentEntity) {
        scheduledPaymentDao.insertPayment(payment)
    }

    suspend fun deletePaymentById(id: Int) {
        scheduledPaymentDao.deletePaymentById(id)
    }

    suspend fun updatePayment(payment: ScheduledPaymentEntity) {
        scheduledPaymentDao.updatePayment(payment)
    }

    suspend fun getPaymentById(id: Int): ScheduledPaymentEntity? {
        return scheduledPaymentDao.getPaymentById(id)
    }
}