package com.checkinface.util

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Date

class FirestoreEventHelper {
    private val db = Firebase.firestore

    companion object {
        private const val COURSE_COLLECTION = "course"
        private const val CODE_FIELD = "course_code"
    }


    fun addEvent(courseCode: String, date: String, startTime: String, lateTime: String, absentTime: String,
                 useGeolocation: Boolean, patternLock:String?, useQR: Boolean,
                 onSuccessListener: () -> Unit) {
        val event = hashMapOf(
            "event_date" to date,
            "event_start_time" to startTime,
            "event_late_time" to lateTime,
            "event_absent_time" to absentTime,
            "use_geolocation" to useGeolocation,
            "pattern_lock" to patternLock,
            "use_qr" to useQR,
        )

        db.collection(COURSE_COLLECTION)
            .whereEqualTo(CODE_FIELD, courseCode)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val events = document.reference.collection("events")
                    events.add(event)
                        .addOnSuccessListener {
                            onSuccessListener()
                        }
                }
            }
    }
}