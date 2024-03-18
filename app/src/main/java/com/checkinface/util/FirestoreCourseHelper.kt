package com.checkinface.util

import android.util.Log
import android.widget.Toast
import com.checkinface.fragment.dashboard.DashboardModel
import com.checkinface.fragment.student_attendance_list.AttendanceStatus
import com.checkinface.fragment.teacher_course.attendance_detail_student_list.AttendanceDetailStudentModel
import com.checkinface.fragment.teacher_course.student_list.StudentModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.QuerySnapshot
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
        private const val USER_COLLECTION = "users"
        private const val NAME_FIELD = "course_name"
        private const val COLOR_FIELD = "course_color"
        private const val NEXT_CHECK_TIME_FIELD = "next_check_time"
        private const val STUDENT_COUNT_FIELD = "student_count"
        private const val COURSE_CODE = "course_code"
        private const val TEACHER_FIELD = "course_teacher"
        private const val STUDENT_FIELD = "student"
        private const val STATUS_FIELD = "status"
        private const val EMAIL_FIELD = "email"
        private const val STUDENT_NAME = "student_name"
        private const val STUDENT_EMAIL = "student_email"
        private const val STUDENT_ID = "student_id"
    }
    suspend fun getCourses(email: String, role: UserRole): ArrayList<DashboardModel> {
        val data: ArrayList<DashboardModel> = ArrayList()

        var courses: QuerySnapshot? = null

        if(role == UserRole.TEACHER) {
            // Search Course that contains the email
            courses = db.collection(COURSE_COLLECTION)
                .whereEqualTo(TEACHER_FIELD, email)
                .get()
                .await()

            // Add to data
            if(courses != null) {
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
            }
        } else if(role == UserRole.STUDENT) {
            // Get All Courses
            courses = db.collection(COURSE_COLLECTION)
                .get()
                .await()

            // Collect student where the email is found
            val studentCourse = mutableListOf<String>()
            for (course in courses.documents) {
                val courseId = course.id
                val studentQuery = db.collection(COURSE_COLLECTION)
                    .document(courseId)
                    .collection(STUDENT_COLLECTION)
                    .whereEqualTo(STUDENT_EMAIL, email)
                    .get()
                    .await()
                if (!studentQuery.isEmpty) {
                    studentCourse.add(courseId)
                }
            }

            for (id in studentCourse) {
                val course = db.collection(COURSE_COLLECTION)
                    .document(id)
                    .get()
                    .await()
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

        }

        return data
    }

    fun addCourse(courseName: String, teacherEmail: String, onSuccessListener: (courseCode: String) -> Unit, onFailureListener: (e: Exception) -> Unit) {
        val course = hashMapOf(
            NAME_FIELD to courseName,
            COLOR_FIELD to CourseUtil.generateColor(),
            TEACHER_FIELD to teacherEmail,
            STUDENT_COUNT_FIELD to 0,
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
                    .addOnFailureListener { e ->
                        onFailureListener(e)
                    }
            }
    }

    suspend fun addStudent(courseCode: String, onSuccessListener: (() -> Unit)? = null, onFailureListener: ((e: Exception) -> Unit)? = null) {
        // get student id
        val studentId = db.collection(USER_COLLECTION)
            .whereEqualTo(EMAIL_FIELD, Firebase.auth.currentUser?.email.toString())
            .get()
            .await()
            .documents
            .get(0)
            .get(STUDENT_ID)

        val student = hashMapOf(
            STUDENT_EMAIL to Firebase.auth.currentUser?.email.toString(),
            STUDENT_NAME to Firebase.auth.currentUser?.displayName.toString(),
            STUDENT_ID to studentId
        )

        // this subscribe the student to all add/delete/modify of attendance event of this class
        FirebaseMessaging.getInstance().subscribeToTopic("${courseCode}_event")

        // Add Student to Course
        db.collection(COURSE_COLLECTION)
            .whereEqualTo(COURSE_CODE, courseCode)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val students = document.reference.collection(STUDENT_COLLECTION)
                    students.add(student)
                        .addOnSuccessListener {
                            if (onSuccessListener != null) {
                                onSuccessListener()
                            }
                        }
                        .addOnFailureListener { e ->
                            if (onFailureListener != null) {
                                onFailureListener(e)
                            }
                            Log.e("Firestore","Error adding student document to course document: $e")
                        }
                }
            }

        // Increase student count
        // Get Course Id
        val id = db.collection(COURSE_COLLECTION)
            .whereEqualTo(COURSE_CODE, courseCode)
            .get()
            .await()
            .documents.get(0).id

        var studentCount = db.collection(COURSE_COLLECTION)
            .document(id)
            .get()
            .await()
            .get(STUDENT_COUNT_FIELD)
            .toString()
            .toInt() + 1

        db.collection(COURSE_COLLECTION)
            .document(id)
            .update(STUDENT_COUNT_FIELD, studentCount)
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
            .orderBy(STUDENT_EMAIL)
            .get()
            .await()

        // Add to data for student with existing attendance records
        var presentCount = 0;
        var absentCount = 0;
        var lateCount = 0;

        for (student in students.documents) {
            for (attendance in attendances.documents) {
                // Current Student
                if(student.get(STUDENT_EMAIL) == attendance.get(STUDENT_FIELD)) {
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
                    id = student.get(STUDENT_ID)?.toString(),
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