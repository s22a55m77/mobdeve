package com.checkinface.fragment.course

import java.util.Date

data class AttendanceModel(
    val date: Date,
    val status: CourseStatus
)