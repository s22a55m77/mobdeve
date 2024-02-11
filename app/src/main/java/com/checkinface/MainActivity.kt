package com.checkinface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.checkinface.dashboard.DashboardActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, InitializeActivity::class.java)
        startActivity(intent)
    }
}