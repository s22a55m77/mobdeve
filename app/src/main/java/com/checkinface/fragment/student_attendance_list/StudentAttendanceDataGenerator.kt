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
                    DateUtil.getDate("2024-02-13T07:00:01+08:00"),
                    AttendanceStatus.ABSENT
                )
            )
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-14T07:00:01+08:00"),
                    AttendanceStatus.LATE
                )
            )
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-16T07:00:01+08:00"),
                    AttendanceStatus.PRESENT
                )
            )
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-18T07:00:01+08:00"),
                    AttendanceStatus.PRESENT
                )
            )
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-19T07:00:01+08:00"),
                    AttendanceStatus.PRESENT
                )
            )
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-20T07:00:01+08:00"),
                    AttendanceStatus.PRESENT
                )
            )
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-21T07:00:01+08:00"),
                    AttendanceStatus.ABSENT
                )
            )
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-22T07:00:01+08:00"),
                    AttendanceStatus.PRESENT
                )
            )
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-23T07:00:01+08:00"),
                    AttendanceStatus.ABSENT
                )
            )
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-24T07:00:01+08:00"),
                    AttendanceStatus.LATE
                )
            )
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-25T07:00:01+08:00"),
                    AttendanceStatus.PRESENT
                )
            )
            data.add(
                StudentAttendanceModel(
                    DateUtil.getDate("2024-02-26T07:00:01+08:00"),
                    AttendanceStatus.PRESENT
                )
            )
            return data
        }
    }
}