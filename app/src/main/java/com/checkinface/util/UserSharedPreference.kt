package com.checkinface.util

import android.content.Context
import com.google.firebase.auth.FirebaseUser

class UserSharedPreference(context: Context) {
    private val PREFERENCE_NAME = "CheckInFace"
    private val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun saveUserData(user: FirebaseUser, role: UserRole) {
        val editor = sharedPreferences.edit()
        editor.putString("displayName", user.displayName)
        editor.putString("email", user.email)
        editor.putString("role", role.toString())
        editor.apply()
    }

    fun getUserData(): Triple<String?, String?, UserRole?> {
        val displayName = sharedPreferences.getString("displayName", null)
        val email = sharedPreferences.getString("email", null)
        val roleString = sharedPreferences.getString("role", null)
        val role = if (roleString != null) UserRole.valueOf(roleString) else null
        return Triple(displayName, email, role)
    }
}