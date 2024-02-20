package com.checkinface.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.checkinface.databinding.ActivityInitializeBinding
import com.checkinface.util.UserRole
import com.checkinface.util.UserSharedPreference

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
            userPreference.setRole(UserRole.STUDENT)
            startActivity(intentToDashboard)
        }

        viewBinding.btnTeacher.setOnClickListener {
            val intentToDashboard = Intent(this@InitializeActivity, MainActivity::class.java)
            userPreference.setRole(UserRole.TEACHER)
            startActivity(intentToDashboard)
        }
    }
}