package com.checkinface.fragment.teacher_course.attendance_list

import com.checkinface.util.DateUtil

class AttendanceDataGenerator {
    companion object {
        fun loadData(): ArrayList<TeacherAttendanceModel> {
            val data = ArrayList<TeacherAttendanceModel>()
            data.add(
                TeacherAttendanceModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    3,
                    2,
                    1,
                )
            )
            data.add(
                TeacherAttendanceModel(
                    DateUtil.getDate("2024-02-13T07:00:01+08:00"),
                    1,
                    4,
                    2,
                )
            )
            data.add(
                TeacherAttendanceModel(
                    DateUtil.getDate("2024-02-16T07:00:01+08:00"),
                    12,
                    5,
                    4,
                )
            )
            data.add(
                TeacherAttendanceModel(
                    DateUtil.getDate("2024-02-19T07:00:01+08:00"),
                    63,
                    23,
                    12,
                )
            )
            data.add(
                TeacherAttendanceModel(
                    DateUtil.getDate("2024-02-20T07:00:01+08:00"),
                    0,
                    0,
                    0,
                )
            )
            return data
        }
    }
}
