package com.checkinface.fragment.teacher_course.attendance_detail_student_list

import com.checkinface.fragment.student_attendance_list.AttendanceStatus

class AttendanceDetailStudentListDataGenerator {
    companion object {
        fun loadData(): ArrayList<AttendanceDetailStudentModel> {
            val data: ArrayList<AttendanceDetailStudentModel> = ArrayList()
            data.add(
                AttendanceDetailStudentModel(
                    "Student 1",
                    AttendanceStatus.PRESENT
                )
            )
            data.add(
                AttendanceDetailStudentModel(
                    "Student 2",
                    AttendanceStatus.ABSENT
                )
            )
            data.add(
                AttendanceDetailStudentModel(
                    "Student 3",
                    AttendanceStatus.LATE
                )
            )
            data.add(
                AttendanceDetailStudentModel(
                    "Student 4",
                    AttendanceStatus.PRESENT
                )
            )
            data.add(
                AttendanceDetailStudentModel(
                    "Student 5",
                    AttendanceStatus.PRESENT
                )
            )
            data.add(
                AttendanceDetailStudentModel(
                    "Student 6",
                    AttendanceStatus.LATE
                )
            )
            data.add(
                AttendanceDetailStudentModel(
                    "Student 7",
                    AttendanceStatus.ABSENT
                )
            )
            data.add(
                AttendanceDetailStudentModel(
                    "Student 8",
                    AttendanceStatus.PRESENT
                )
            )
            data.add(
                AttendanceDetailStudentModel(
                    "Student 9",
                    AttendanceStatus.ABSENT
                )
            )
            data.add(
                AttendanceDetailStudentModel(
                    "Student 10",
                    AttendanceStatus.LATE
                )
            )
            data.add(
                AttendanceDetailStudentModel(
                    "Student 11",
                    AttendanceStatus.PRESENT
                )
            )
            data.add(
                AttendanceDetailStudentModel(
                    "Student 12",
                    AttendanceStatus.PRESENT
                )
            )

            return data
        }
    }
}