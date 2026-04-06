package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.local.entity.ScheduledPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduledPaymentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: ScheduledPaymentEntity)

    @Query("SELECT * FROM scheduled_payments ORDER BY id DESC")
    fun getAllPayments(): Flow<List<ScheduledPaymentEntity>>

    @Query("DELETE FROM scheduled_payments WHERE id = :id")
    suspend fun deletePaymentById(id: Int)

    @Update
    suspend fun updatePayment(payment: ScheduledPaymentEntity)

    @Query("SELECT * FROM scheduled_payments WHERE id = :id LIMIT 1")
    suspend fun getPaymentById(id: Int): ScheduledPaymentEntity?
}