package com.checkinface.util

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FirestoreUserHelper {
    private val db = Firebase.firestore

    companion object {
        const val USERS_COLLECTION = "users"
        const val EMAIL_FIELD = "email"
        const val ROLE_FIELD = "role"
    }

    fun addUser(email: String, role: UserRole, onSuccessListener: () -> Unit) {
        val user = hashMapOf(
            EMAIL_FIELD to email,
            ROLE_FIELD to role
        )
        db.collection(USERS_COLLECTION)
            .add(user)
            .addOnSuccessListener {
                onSuccessListener()
            }
    }

    fun getUserByEmail(email: String, onFound: () -> Unit, onNotFound: () -> Unit) {
        db.collection(USERS_COLLECTION)
            .whereEqualTo(EMAIL_FIELD, email)
            .get()
            .addOnSuccessListener { document ->
                if(document != null && !document.isEmpty)
                    onFound()
                else
                    onNotFound()
            }
    }
}