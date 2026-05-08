package com.example.myapplication.data

import android.content.Context

class UserSessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun rememberDevice(email: String, userId: Int) {
        prefs.edit()
            .putString(KEY_REMEMBERED_EMAIL, email)
            .putInt(KEY_USER_ID, userId)
            .putBoolean(KEY_KNOWN_DEVICE, true)
            .apply()
    }

    fun getRememberedEmail(): String? {
        return prefs.getString(KEY_REMEMBERED_EMAIL, null)
    }

    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun isKnownDevice(): Boolean {
        return prefs.getBoolean(KEY_KNOWN_DEVICE, false) &&
                !getRememberedEmail().isNullOrBlank() &&
                getUserId() != -1
    }

    fun forgetDevice() {
        prefs.edit()
            .remove(KEY_REMEMBERED_EMAIL)
            .remove(KEY_USER_ID)
            .remove(KEY_KNOWN_DEVICE)
            .apply()
    }

    companion object {
        private const val KEY_REMEMBERED_EMAIL = "remembered_email"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_KNOWN_DEVICE = "known_device"
    }
}