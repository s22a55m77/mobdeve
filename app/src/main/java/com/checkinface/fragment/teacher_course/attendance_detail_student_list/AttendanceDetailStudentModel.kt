package com.checkinface.fragment.teacher_course.attendance_detail_student_list

import com.checkinface.fragment.student_attendance_list.AttendanceStatus

data class AttendanceDetailStudentModel(
    val name: String,
    val email: String,
    var status: AttendanceStatus
)
