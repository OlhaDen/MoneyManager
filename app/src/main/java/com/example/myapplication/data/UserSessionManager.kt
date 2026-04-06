package com.example.myapplication.data

import android.content.Context

class UserSessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveLoggedInUser(email: String) {
        prefs.edit().putString(KEY_CURRENT_USER_EMAIL, email).apply()
    }

    fun getLoggedInUserEmail(): String? {
        return prefs.getString(KEY_CURRENT_USER_EMAIL, null)
    }

    fun isLoggedIn(): Boolean {
        return !getLoggedInUserEmail().isNullOrBlank()
    }

    fun logout() {
        prefs.edit().remove(KEY_CURRENT_USER_EMAIL).apply()
    }

    companion object {
        private const val KEY_CURRENT_USER_EMAIL = "current_user_email"
    }
}