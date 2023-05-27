package com.example.listed.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class AuthManager @Inject constructor(context: Context) {
    private val PREF_NAME = "TokenPrefs"
    private val KEY_AUTH_TOKEN = "AuthToken"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    fun setToken(token: String) {
        sharedPreferences.edit().putString(KEY_AUTH_TOKEN, token).commit()
    }
}