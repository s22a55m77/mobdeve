package com.checkinface.util

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FirestoreEventHelper {
    private val db = Firebase.firestore

    companion object {
        private const val COURSE_COLLECTION = "course"
        private const val EVENT_COLLECTION = "events"
        private const val CODE_FIELD = "course_code"
        private const val START_TIME = "event_start_time"
    }

    suspend fun getEventDetailBasedOnEvent(courseCode: String, eventTime: String): MutableMap<String, Any>? {
        // Get Course Id
        val id = db.collection(COURSE_COLLECTION)
            .whereEqualTo(CODE_FIELD, courseCode)
            .get()
            .await()
            .documents.get(0).id

        // Get Events
        val events = db.collection(COURSE_COLLECTION)
            .document(id)
            .collection(EVENT_COLLECTION)
            .whereEqualTo(START_TIME, eventTime)
            .get()
            .await()

        return events.documents[0].data
    }

    fun addEvent(courseCode: String, startTime: String, lateTime: String, absentTime: String,
                 useGeolocation: Boolean, patternLock:String?, useQR: Boolean,
                 onSuccessListener: () -> Unit, onFailureListener: (e: Exception) -> Unit) {
        val event = hashMapOf(
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
                    val events = document.reference.collection(EVENT_COLLECTION)
                    events.add(event)
                        .addOnSuccessListener {
                            onSuccessListener()
                        }
                }
            }
            .addOnFailureListener { e ->
                if(onFailureListener != null)
                    onFailureListener(e)
            }

    }

    suspend fun deleteEvent(courseCode: String, eventTime: String, onSuccessListener: () -> Unit, onFailureListener: (e: Exception) -> Unit) {
        // Get Course Id
        val id = db.collection(COURSE_COLLECTION)
            .whereEqualTo(CODE_FIELD, courseCode)
            .get()
            .await()
            .documents.get(0).id

        // Get Event
        val event = db.collection(COURSE_COLLECTION)
            .document(id)
            .collection(EVENT_COLLECTION)
            .whereEqualTo(START_TIME, eventTime)
            .get()
            .await()
            .documents.get(0).id

        // Delete
        db.collection(COURSE_COLLECTION)
            .document(id)
            .collection(EVENT_COLLECTION)
            .document(event)
            .delete()
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }
    }

    suspend fun updateEvent(courseCode: String, origEventTime: String, startTime: String, lateTime: String, absentTime: String,
                            useGeolocation: Boolean, patternLock:String?, useQR: Boolean,
                            onSuccessListener: () -> Unit, onFailureListener: ((e: Exception) -> Unit)? = null) {

        val event = mutableMapOf<String, Any?>(
            "event_start_time" to startTime,
            "event_late_time" to lateTime,
            "event_absent_time" to absentTime,
            "use_geolocation" to useGeolocation,
            "pattern_lock" to patternLock,
            "use_qr" to useQR
        )

        // Get Course Id
        val id = db.collection(COURSE_COLLECTION)
            .whereEqualTo(CODE_FIELD, courseCode)
            .get()
            .await()
            .documents.get(0).id

        // Get Event
        val eventId = db.collection(COURSE_COLLECTION)
            .document(id)
            .collection(EVENT_COLLECTION)
            .whereEqualTo(START_TIME, origEventTime)
            .get()
            .await()
            .documents.get(0).id

        db.collection(COURSE_COLLECTION)
            .document(id)
            .collection(EVENT_COLLECTION)
            .document(eventId)
            .update(event)
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener { e ->
                if(onFailureListener != null)
                    onFailureListener(e)
            }
    }
}