package com.checkinface.util

import android.util.Log
import android.widget.Toast
import com.checkinface.fragment.dashboard.DashboardModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.util.Date

class FirestoreCourseHelper {
    private val db = Firebase.firestore

    companion object {
        private const val COURSE_COLLECTION = "course"
        private const val NAME_FIELD = "course_name"
        private const val COLOR_FIELD = "course_color"
        private const val NEXT_CHECK_TIME_FIELD = "next_check_time"
        private const val STUDENT_COUNT_FIELD = "student_count"
        private const val CODE_FIELD = "course_code"
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
                    studentCount
                )
            )
        }

        return data
    }

    fun addCourse(courseName: String, onSuccessListener: (courseCode: String) -> Unit) {
        val db = Firebase.firestore
        val course = hashMapOf(
            NAME_FIELD to courseName,
            COLOR_FIELD to CourseUtil.generateColor()
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
                    .update(CODE_FIELD, courseCode)
                    .addOnSuccessListener {
                        onSuccessListener(courseCode!!)
                    }
            }
    }

    fun addStudent(courseCode: String, onSuccessListener: () -> Unit, onFailureListener: () -> Unit) {
        val db = Firebase.firestore
        val student = hashMapOf(
            "student_email" to Firebase.auth.currentUser?.email.toString(),
        )
        db.collection("course")
            .whereEqualTo(CODE_FIELD, courseCode)
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
}