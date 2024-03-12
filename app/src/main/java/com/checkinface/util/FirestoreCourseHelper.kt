package com.checkinface.util

import android.util.Log
import android.widget.Toast
import com.checkinface.fragment.dashboard.DashboardModel
import com.checkinface.fragment.student_attendance_list.AttendanceStatus
import com.checkinface.fragment.teacher_course.attendance_detail_student_list.AttendanceDetailStudentModel
import com.checkinface.fragment.teacher_course.student_list.StudentModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import java.util.Date

class FirestoreCourseHelper {
    private val db = Firebase.firestore

    companion object {
        private const val COURSE_COLLECTION = "course"
        private const val ATTENDANCE_COLLECTION = "attendances"
        private const val STUDENT_COLLECTION = "students"
        private const val NAME_FIELD = "course_name"
        private const val COLOR_FIELD = "course_color"
        private const val NEXT_CHECK_TIME_FIELD = "next_check_time"
        private const val STUDENT_COUNT_FIELD = "student_count"
        private const val COURSE_CODE = "course_code"
        private const val TEACHER_FIELD = "course_teacher"
        private const val STUDENT_FIELD = "student"
        private const val STATUS_FIELD = "status"
        private const val STUDENT_NAME = "student_name"
        private const val STUDENT_EMAIL = "student_email"
    }
    suspend fun getCourses(): ArrayList<DashboardModel> {
        val data: ArrayList<DashboardModel> = ArrayList()
        val courses = db.collection(COURSE_COLLECTION)
            .get()
            .await()

        for (course in courses) {
            val date: Date?
            val studentCount: Number?
            if(course.get(NEXT_CHECK_TIME_FIELD) != null)
                date = DateUtil.getDate(course.get(NEXT_CHECK_TIME_FIELD).toString())
            else
                date = null
            if(course.get(STUDENT_COUNT_FIELD) != null)
                studentCount = course.get(STUDENT_COUNT_FIELD).toString().toInt()
            else
                studentCount = null
            data.add(
                DashboardModel(
                    course.get(NAME_FIELD).toString(),
                    course.get(COLOR_FIELD).toString(),
                    date,
                    studentCount,
                    course.get(COURSE_CODE).toString(),
                )
            )
        }

        return data
    }

    fun addCourse(courseName: String, teacherEmail: String, onSuccessListener: (courseCode: String) -> Unit) {
        val course = hashMapOf(
            NAME_FIELD to courseName,
            COLOR_FIELD to CourseUtil.generateColor(),
            TEACHER_FIELD to teacherEmail
        )
        var courseCode: String? = null;
        // Add Course
        db.collection(COURSE_COLLECTION)
            .add(course)
            .addOnSuccessListener { documentReference ->
                // Generate Course Code based on Id
                courseCode = CourseUtil.generateCode(documentReference.id)
                // Update with Course Code
                db.document(documentReference.path)
                    .update(COURSE_CODE, courseCode)
                    .addOnSuccessListener {
                        onSuccessListener(courseCode!!)
                    }
            }
    }

    fun addStudent(courseCode: String, onSuccessListener: () -> Unit, onFailureListener: () -> Unit) {
        val student = hashMapOf(
            "student_email" to Firebase.auth.currentUser?.email.toString(),
            "student_name" to Firebase.auth.currentUser?.displayName.toString()
        )

        // this subscribe the student to all add/delete/modify of attendance event of this class
        FirebaseMessaging.getInstance().subscribeToTopic("${courseCode}_event")

        db.collection("course")
            .whereEqualTo(COURSE_CODE, courseCode)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val students = document.reference.collection("students")
                    students.add(student)
                        .addOnSuccessListener {
                            onSuccessListener()
                        }
                        .addOnFailureListener { e ->
                            onFailureListener()
                            Log.e("Firestore","Error adding student document to course document: $e")
                        }
                }
            }
    }

    suspend fun getStudentLists(courseCode: String): ArrayList<StudentModel> {
        val data: ArrayList<StudentModel> = arrayListOf()
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
            .orderBy("student_email")
            .get()
            .await()

        // Add to data for student with existing attendance records
        var presentCount = 0;
        var absentCount = 0;
        var lateCount = 0;

        for (student in students.documents) {
            for (attendance in attendances.documents) {
                // Current Student
                if(student.get("student_email") == attendance.get(STUDENT_FIELD)) {
                    when(attendance.get(STATUS_FIELD)) {
                        "PRESENT" -> presentCount++
                        "LATE" -> lateCount++
                        "ABSENT" -> absentCount++
                    }
                }
            }
            // add to data
            data.add(
                StudentModel(
                    name = student.get(STUDENT_NAME).toString(),
                    email = student.get(STUDENT_EMAIL).toString(),
                    presentCount = presentCount,
                    absentCount = absentCount,
                    lateCount = lateCount,
                )
            )
            // reset
            presentCount = 0;
            absentCount = 0;
            lateCount = 0;
        }
        return data
    }
}