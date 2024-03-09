package com.checkinface.util

import com.checkinface.fragment.dashboard.DashboardModel
import com.google.firebase.Firebase
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
    }
    suspend fun getCourses(): ArrayList<DashboardModel> {
        val data: ArrayList<DashboardModel> = ArrayList()
        val courses = db.collection(COURSE_COLLECTION)
            .get()
            .await()

        for (course in courses) {
            val date: Date?
            val studentCount: Number?
            if(course.get("next_check_time") != null)
                date = DateUtil.getDate(course.get("next_check_time").toString())
            else
                date = null
            if(course.get("student_count") != null)
                studentCount = course.get("student_count").toString().toInt()
            else
                studentCount = null
            data.add(
                DashboardModel(
                    course.get("course_name").toString(),
                    course.get("course_color").toString(),
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
            "course_name" to courseName,
            "course_color" to CourseUtil.generateColor()
        )
        var courseCode: String? = null;
        // Add Course
        db.collection("course")
            .add(course)
            .addOnSuccessListener { documentReference ->
                // Generate Course Code based on Id
                courseCode = CourseUtil.generateCode(documentReference.id)
                // Update with Course Code
                db.document(documentReference.path)
                    .update("course_code", courseCode)
                    .addOnSuccessListener {
                        onSuccessListener(courseCode!!)
                    }
            }
    }

}