package com.example.myapplication.data

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.ScheduledPaymentRepository
import com.example.myapplication.data.repository.TransactionRepository

class AppContainer(context: Context) {

    private val database: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "expense_tracker_db"
    ).fallbackToDestructiveMigration()
        .build()

    val authRepository: AuthRepository by lazy {
        AuthRepository(database.userDao())
    }

    val transactionRepository: TransactionRepository by lazy {
        TransactionRepository(database.transactionDao())
    }

    val scheduledPaymentRepository: ScheduledPaymentRepository by lazy {
        ScheduledPaymentRepository(database.scheduledPaymentDao())
    }

    val userSessionManager: UserSessionManager by lazy {
        UserSessionManager(context)
    }
}