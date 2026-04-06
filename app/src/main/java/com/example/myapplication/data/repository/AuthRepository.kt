package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.local.entity.UserEntity

class AuthRepository(
    private val userDao: UserDao
) {
    suspend fun registerUser(email: String, pin: String): Result<Unit> {
        return try {
            val exists = userDao.userExists(email)
            if (exists) {
                Result.failure(IllegalStateException("Email already registered"))
            } else {
                userDao.insertUser(
                    UserEntity(
                        username = email,
                        email = email,
                        pin = pin
                    )
                )
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkIfUserExists(email: String): Boolean {
        return userDao.userExists(email)
    }

    suspend fun verifyPin(email: String, pin: String): Boolean {
        val user = userDao.getUserByEmail(email)
        return user?.pin == pin
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }
}