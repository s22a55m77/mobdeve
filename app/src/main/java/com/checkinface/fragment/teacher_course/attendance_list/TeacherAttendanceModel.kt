package com.checkinface.fragment.teacher_course.attendance_list

import java.util.Date

data class TeacherAttendanceModel (
    val date: Date,
    val presentCount: Number,
    val absentCount: Number,
    val lateCount: Number
)