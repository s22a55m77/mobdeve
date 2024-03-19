package com.checkinface.util;

import com.checkinface.fragment.student_attendance_list.AttendanceStatus
import com.checkinface.fragment.student_attendance_list.StudentAttendanceModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class FirestoreStudentHelper {
    private val db = Firebase.firestore

    companion object {
        private const val COURSE_COLLECTION = "course"
        private const val ATTENDANCE_COLLECTION = "attendances"
        private const val STUDENT_FIELD = "student"
        private const val DATE_FIELD = "event"
        private const val STATUS_FIELD = "status"
        private const val COURSE_CODE = "course_code"
    }

    suspend fun getAttendance(courseCode: String, email: String):ArrayList<StudentAttendanceModel> {
        val data: ArrayList<StudentAttendanceModel> = arrayListOf()
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
            .whereEqualTo(STUDENT_FIELD, email)
            .orderBy(DATE_FIELD)
            .get()
            .await()

        for (event in attendances.documents) {
            var status: AttendanceStatus = AttendanceStatus.ABSENT
            when (event.get(STATUS_FIELD)) {
                "PRESENT" -> status = AttendanceStatus.PRESENT
                "LATE" -> status = AttendanceStatus.LATE
                "ABSENT" -> status = AttendanceStatus.ABSENT
            }

            val firestoreEventHelper = FirestoreEventHelper()
            val eventDetail = firestoreEventHelper.getEventFromId(courseCode, event.get(FirestoreAttendanceHelper.EVENT_ID) as String)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = dateFormat.parse(event.get(DATE_FIELD).toString())!!
            val absentTime = dateFormat.parse(eventDetail?.get(FirestoreEventHelper.ABSENT_TIME).toString())!!
            data.add(
                StudentAttendanceModel(
                    date,
                    absentTime,
                    status
                )
            )
        }
        return data
    }
}
