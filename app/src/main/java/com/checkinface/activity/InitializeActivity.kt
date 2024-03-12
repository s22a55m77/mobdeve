package com.checkinface.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.checkinface.databinding.ActivityInitializeBinding
import com.checkinface.util.FirestoreUserHelper
import com.checkinface.util.UserRole
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class InitializeActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityInitializeBinding
    private val firestoreUserHelper: FirestoreUserHelper = FirestoreUserHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityInitializeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // button for choosing of role
        viewBinding.btnStudent.setOnClickListener {
            val intentToDashboard = Intent(this@InitializeActivity, MainActivity::class.java)
            firestoreUserHelper.addUser(
                Firebase.auth.currentUser?.email!!,
                UserRole.STUDENT,
                Firebase.auth.currentUser?.displayName!!,
                onSuccessListener = {
                    startActivity(intentToDashboard)
                }
            )
        }

        viewBinding.btnTeacher.setOnClickListener {
            val intentToDashboard = Intent(this@InitializeActivity, MainActivity::class.java)
            firestoreUserHelper.addUser(
                Firebase.auth.currentUser?.email!!,
                UserRole.TEACHER,
                Firebase.auth.currentUser?.displayName!!,
                onSuccessListener = {
                    startActivity(intentToDashboard)
                }
            )
        }
    }
}