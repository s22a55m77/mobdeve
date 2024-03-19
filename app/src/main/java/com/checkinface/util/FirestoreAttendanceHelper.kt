package com.checkinface.util

import com.checkinface.fragment.student_attendance_list.AttendanceStatus
import com.checkinface.fragment.teacher_course.attendance_detail_student_list.AttendanceDetailStudentModel
import com.checkinface.fragment.teacher_course.attendance_list.TeacherAttendanceModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class FirestoreAttendanceHelper {
    private val db = Firebase.firestore

    companion object {
        const val COURSE_COLLECTION = "course"
        const val ATTENDANCE_COLLECTION = "attendances"
        const val STUDENT_COLLECTION = "students"
        const val EVENT_COLLECTION = "events"
        const val COURSE_CODE = "course_code"
        const val STUDENT_FIELD = "student"
        const val STATUS_FIELD = "status"
        const val START_TIME = "event_start_time"
        const val EVENT_TIME = "event"
        const val STUDENT_EMAIL = "student_email"
        const val STUDENT_NAME = "student_name"
        const val EVENT_ID = "event_id"
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
                            email = student.get(STUDENT_EMAIL).toString(),
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
                    event.id,
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

    suspend fun updateAttendance(courseCode: String, studentEmail: String, eventTime: String, status: String,
                                 onSuccessListener: () -> Unit, onFailureListener: (e: Exception) -> Unit) {
        // Get Course Id
        val id = db.collection(COURSE_COLLECTION)
            .whereEqualTo(COURSE_CODE, courseCode)
            .get()
            .await()
            .documents.get(0).id

        // Get attendances
        val attendances = db.collection(COURSE_COLLECTION)
            .document(id)
            .collection(ATTENDANCE_COLLECTION)
            .get()
            .await()

        // Get student attendance
        val attendance = attendances.query
            .whereEqualTo(STUDENT_FIELD, studentEmail)
            .whereEqualTo(EVENT_TIME, eventTime)
            .get()
            .await()
            .documents.get(0).id

        db.collection(COURSE_COLLECTION)
            .document(id)
            .collection(ATTENDANCE_COLLECTION)
            .document(attendance)
            .update(STATUS_FIELD, status)
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }
    }

    suspend fun getAttendanceByEmailAndEventId(courseCode: String, eventId: String, email: String): MutableMap<String, Any>? {
        // Get Course Id
        val id = db.collection(COURSE_COLLECTION)
            .whereEqualTo(COURSE_CODE, courseCode)
            .get()
            .await()
            .documents.get(0).id

        val event = db.collection(COURSE_COLLECTION)
            .document(id)
            .collection(ATTENDANCE_COLLECTION)
            .whereEqualTo(EVENT_ID, eventId)
            .whereEqualTo(STUDENT_FIELD, email)
            .get()
            .await()
        return event.documents[0].data
    }
}