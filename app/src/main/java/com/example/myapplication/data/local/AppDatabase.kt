package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.local.dao.ScheduledPaymentDao
import com.example.myapplication.data.local.dao.TransactionDao
import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.local.entity.ScheduledPaymentEntity
import com.example.myapplication.data.local.entity.TransactionEntity
import com.example.myapplication.data.local.entity.UserEntity

@Database(
    entities = [
        com.example.myapplication.data.local.entity.UserEntity::class,
        com.example.myapplication.data.local.entity.TransactionEntity::class,
        com.example.myapplication.data.local.entity.ScheduledPaymentEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
    abstract fun scheduledPaymentDao(): ScheduledPaymentDao
}