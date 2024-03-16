package com.checkinface.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class SettingSharedPreference(context: Context) {
    companion object {
        const val SETTING_KEY = "setting"
        const val NOTIFICATION_KEY = "notification"
    }

    private var sp: SharedPreferences = context.getSharedPreferences(SETTING_KEY, Context.MODE_PRIVATE)

    fun setNotification(state: Boolean) {
        with(sp.edit()) {
            putBoolean(NOTIFICATION_KEY, state)
            apply()
        }
    }

    fun isNotification(): Boolean {
        return sp.getBoolean(NOTIFICATION_KEY, false)
    }
}