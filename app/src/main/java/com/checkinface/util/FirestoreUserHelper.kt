package com.checkinface.util

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FirestoreUserHelper {
    private val db = Firebase.firestore

    companion object {
        const val USERS_COLLECTION = "users"
        const val USER_COLLECTION = "users"
        const val EMAIL_FIELD = "email"
        const val ROLE_FIELD = "role"
        const val NAME_FIELD= "name"
        const val STUDENT_ID = "student_id"
    }

    fun addUser(email: String, role: UserRole, name: String, onSuccessListener: () -> Unit) {
        val user = hashMapOf(
            EMAIL_FIELD to email,
            ROLE_FIELD to role,
            NAME_FIELD to name
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

    suspend fun getRole(): UserRole? {
        val currentUserEmail = Firebase.auth.currentUser?.email
        if (currentUserEmail != null) {
            val querySnapshot = db.collection(USERS_COLLECTION).whereEqualTo(EMAIL_FIELD, currentUserEmail).get().await()
            if (!querySnapshot.isEmpty) {
                val roleString = querySnapshot.documents[0].getString(ROLE_FIELD)
                return if (roleString != null) UserRole.valueOf(roleString) else null
            }
        }
        return null
    }

    suspend fun getId(email: String): String? {
        // get student
        val id = db.collection(USER_COLLECTION)
            .whereEqualTo(EMAIL_FIELD, email)
            .get()
            .await()
            .documents.get(0)
            .get(STUDENT_ID)
            .toString()

        return if(id == "null")
            null
        else
            id
    }

    suspend fun updateId(email: String, updatedId: String, onSuccessListener: () -> Unit, onFailureListener: (e: Exception) -> Unit) {
        // get student
        val id = db.collection(USER_COLLECTION)
            .whereEqualTo(EMAIL_FIELD, email)
            .get()
            .await()
            .documents.get(0).id

        db.collection(USER_COLLECTION)
            .document(id)
            .update(STUDENT_ID, updatedId)
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }
    }
}