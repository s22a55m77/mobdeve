package com.checkinface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.appbar.MaterialToolbar

class CourseActivity : AppCompatActivity() {
    lateinit var tv_appbar: MaterialToolbar;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        this.tv_appbar = findViewById(R.id.appbar_top)
        tv_appbar.title = "Course"
    }
}