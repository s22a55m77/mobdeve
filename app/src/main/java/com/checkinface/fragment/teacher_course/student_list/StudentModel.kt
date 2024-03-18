package com.checkinface.fragment.teacher_course.student_list

data class StudentModel(
    val id: String?,
    val name: String,
    val email: String,
    val presentCount: Number,
    val absentCount: Number,
    val lateCount: Number,
)
