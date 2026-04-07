package com.example.myapplication.data

import android.content.Context

class UserSessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun rememberDevice(email: String) {
        prefs.edit()
            .putString(KEY_REMEMBERED_EMAIL, email)
            .putBoolean(KEY_KNOWN_DEVICE, true)
            .apply()
    }

    fun getRememberedEmail(): String? {
        return prefs.getString(KEY_REMEMBERED_EMAIL, null)
    }

    fun isKnownDevice(): Boolean {
        return prefs.getBoolean(KEY_KNOWN_DEVICE, false) &&
                !getRememberedEmail().isNullOrBlank()
    }

    fun forgetDevice() {
        prefs.edit()
            .remove(KEY_REMEMBERED_EMAIL)
            .remove(KEY_KNOWN_DEVICE)
            .apply()
    }

    companion object {
        private const val KEY_REMEMBERED_EMAIL = "remembered_email"
        private const val KEY_KNOWN_DEVICE = "known_device"
    }
}