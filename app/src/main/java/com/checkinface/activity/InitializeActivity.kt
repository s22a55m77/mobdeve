package com.checkinface.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.checkinface.databinding.ActivityInitializeBinding
import com.checkinface.util.UserRole
import com.checkinface.util.UserSharedPreference
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class InitializeActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityInitializeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityInitializeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val userPreference = UserSharedPreference(this)


        // button for choosing of role
        viewBinding.btnStudent.setOnClickListener {
            val intentToDashboard = Intent(this@InitializeActivity, MainActivity::class.java)
            val db = Firebase.firestore
            val user = hashMapOf(
                "email" to Firebase.auth.currentUser?.email,
                "role" to UserRole.STUDENT
            )
            db.collection("users")
                .add(user)
                .addOnSuccessListener { startActivity(intentToDashboard) }

        }

        viewBinding.btnTeacher.setOnClickListener {
            val intentToDashboard = Intent(this@InitializeActivity, MainActivity::class.java)
            val db = Firebase.firestore
            val user = hashMapOf(
                "email" to Firebase.auth.currentUser?.email,
                "role" to UserRole.TEACHER
            )
            db.collection("users")
                .add(user)
                .addOnSuccessListener { startActivity(intentToDashboard) }
        }
    }
}