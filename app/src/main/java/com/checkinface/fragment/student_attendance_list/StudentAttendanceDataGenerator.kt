package com.checkinface.fragment.student_attendance_list

import com.checkinface.util.DateUtil

class StudentAttendanceDataGenerator {
    companion object {
        fun loadData(): ArrayList<StudentAttendanceModel> {
            val data = ArrayList<StudentAttendanceModel>()
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    AttendanceStatus.PRESENT
                )
            )
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    AttendanceStatus.ABSENT
                )
            )
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    AttendanceStatus.LATE
                )
            )
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    AttendanceStatus.PRESENT
                )
            )
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    AttendanceStatus.PRESENT
                )
            )
            return data
        }
    }
}