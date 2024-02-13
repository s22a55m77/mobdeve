package com.checkinface.fragment.course

import com.checkinface.util.DateUtil

class CourseDataGenerator {
    companion object {
        fun loadData(): ArrayList<AttendanceModel> {
            val data = ArrayList<AttendanceModel>()
            data.add(
                AttendanceModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    CourseStatus.PRESENT
                )
            )
            data.add(
                AttendanceModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    CourseStatus.ABSENT
                )
            )
            data.add(
                AttendanceModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    CourseStatus.LATE
                )
            )
            data.add(
                AttendanceModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    CourseStatus.PRESENT
                )
            )
            data.add(
                AttendanceModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    CourseStatus.PRESENT
                )
            )
            return data
        }
    }
}