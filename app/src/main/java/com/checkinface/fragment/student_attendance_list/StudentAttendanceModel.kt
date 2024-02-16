package com.checkinface.fragment.student_attendance_list

import java.util.Date

data class StudentAttendanceModel(
    val date: Date,
    val status: AttendanceStatus
)