package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.TransactionDao
import com.example.myapplication.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

class TransactionRepository(
    private val transactionDao: TransactionDao
) {
    fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.getAllTransactions()
    }

    suspend fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun deleteTransactionById(id: Int) {
        transactionDao.deleteTransactionById(id)
    }
}