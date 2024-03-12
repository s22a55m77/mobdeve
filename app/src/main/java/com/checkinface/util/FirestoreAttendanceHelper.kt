package com.checkinface.util

import android.util.Log
import com.checkinface.fragment.student_attendance_list.AttendanceStatus
import com.checkinface.fragment.teacher_course.attendance_detail_student_list.AttendanceDetailStudentModel
import com.checkinface.fragment.teacher_course.attendance_list.TeacherAttendanceModel
import com.checkinface.fragment.teacher_course.student_list.StudentModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class FirestoreAttendanceHelper {
    private val db = Firebase.firestore

    companion object {
        private const val COURSE_COLLECTION = "course"
        private const val ATTENDANCE_COLLECTION = "attendances"
        private const val STUDENT_COLLECTION = "students"
        private const val EVENT_COLLECTION = "events"
        private const val COURSE_CODE = "course_code"
        private const val STUDENT_FIELD = "student"
        private const val STATUS_FIELD = "status"
        private const val START_TIME = "event_start_time"
        private const val EVENT_TIME = "event"
        private const val STUDENT_EMAIL = "student_email"
        private const val STUDENT_NAME = "student_name"
    }

    suspend fun getStudentListsBasedOnEvent(courseCode: String, eventTime: String): ArrayList<AttendanceDetailStudentModel> {
        val data: ArrayList<AttendanceDetailStudentModel> = arrayListOf()

        // Get Course Id
        val id = db.collection(COURSE_COLLECTION)
            .whereEqualTo(COURSE_CODE, courseCode)
            .get()
            .await()
            .documents.get(0).id

        // Get Attendances based on Course Id
        val attendances = db.collection(COURSE_COLLECTION)
            .document(id)
            .collection(ATTENDANCE_COLLECTION)
            .orderBy(STUDENT_FIELD)
            .get()
            .await()

        // Get Students
        val students = db.collection(COURSE_COLLECTION)
            .document(id)
            .collection(STUDENT_COLLECTION)
            .orderBy(STUDENT_EMAIL)
            .get()
            .await()

        for (student in students) {
            for (attendance in attendances) {
                // Check if is the correct attendance record
                if (attendance.get(EVENT_TIME) != eventTime)
                    continue
                if (student.get(STUDENT_EMAIL) == attendance.get(STUDENT_FIELD)) {
                    var status: AttendanceStatus = AttendanceStatus.ABSENT
                    when(attendance.get(STATUS_FIELD)) {
                        "PRESENT" -> status = AttendanceStatus.PRESENT
                        "LATE" -> status = AttendanceStatus.LATE
                        "ABSENT" -> status = AttendanceStatus.ABSENT
                    }
                    data.add(
                        AttendanceDetailStudentModel(
                            name = student.get(STUDENT_NAME).toString(),
                            status = status
                        ))
                }
            }
        }
        return data
    }

    suspend fun getEventsBasedOnCourse(courseCode: String): ArrayList<TeacherAttendanceModel> {
        var data: ArrayList<TeacherAttendanceModel> = arrayListOf()
        // Get Course Id
        val id = db.collection(COURSE_COLLECTION)
            .whereEqualTo(COURSE_CODE, courseCode)
            .get()
            .await()
            .documents.get(0).id

        // Get Events
        val events = db.collection(COURSE_COLLECTION)
            .document(id)
            .collection(EVENT_COLLECTION)
            .orderBy(START_TIME)
            .get()
            .await()

        // Get Attendances
        val attendances = db.collection(COURSE_COLLECTION)
            .document(id)
            .collection(ATTENDANCE_COLLECTION)
            .orderBy(EVENT_TIME)
            .get()
            .await()

        var presentCount = 0
        var absentCount = 0
        var lateCount = 0;
        for (event in events) {
            for (attendance in attendances) {
                // current
                if(event.get(START_TIME) == attendance.get(EVENT_TIME)) {
                    when(attendance.get(STATUS_FIELD)) {
                        "PRESENT" -> presentCount++
                        "LATE" -> lateCount++
                        "ABSENT" -> absentCount++
                    }
                }
            }
            // add to data
            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
            val date = dateFormat.parse(event.get(START_TIME).toString())
            data.add(
                TeacherAttendanceModel(
                    date,
                    presentCount,
                    absentCount,
                    lateCount,
                )
            )
            // Reset
            presentCount = 0
            absentCount = 0
            lateCount = 0
        }
        return data
    }
}