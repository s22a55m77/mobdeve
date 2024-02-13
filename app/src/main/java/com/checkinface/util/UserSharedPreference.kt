package com.checkinface.util

import android.content.Context
import com.google.firebase.auth.FirebaseUser

class UserSharedPreference(context: Context) {
    private val PREFERENCE_NAME = "CheckInFace"
    private val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun setRole(role: UserRole) {
        val editor = sharedPreferences.edit()
        editor.putString("role", role.toString())
        editor.apply()
    }

    fun getRole(): UserRole? {
        val roleString = sharedPreferences.getString("role", null)
        return if (roleString != null) UserRole.valueOf(roleString) else null
    }

    fun removeUserData() {
        val editor = sharedPreferences.edit()
        editor.remove("displayName")
        editor.remove("email")
        editor.remove("role")
        editor.apply()
    }
}