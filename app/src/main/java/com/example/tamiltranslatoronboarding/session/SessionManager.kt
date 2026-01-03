package com.example.tamiltranslatoronboarding.session

import android.content.Context

class SessionManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("tamil_session", Context.MODE_PRIVATE)

    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean("is_logged_in", loggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
