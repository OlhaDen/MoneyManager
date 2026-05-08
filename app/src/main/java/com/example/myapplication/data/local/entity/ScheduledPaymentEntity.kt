package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scheduled_payments")
data class ScheduledPaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val amount: Double,
    val category: String,
    val description: String,
    val dueDate: String,
    val type: String,
    val recurring: String,
    val isPaid: Boolean
)