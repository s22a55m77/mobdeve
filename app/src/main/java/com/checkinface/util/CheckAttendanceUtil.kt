package com.checkinface.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.checkinface.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class CheckAttendanceUtil(private val activity: Activity, private val context: Context) {
    private val firestoreEventHelper: FirestoreEventHelper = FirestoreEventHelper()
    private val firestoreAttendanceHelper: FirestoreAttendanceHelper = FirestoreAttendanceHelper()

    private fun checkAttendanceIsOpen(absentTime: String, startTime: String): Boolean {
        if (DateUtil.isPassed(absentTime)) {
            Toast.makeText(context, "Attendance Checking is Closed", Toast.LENGTH_LONG).show()
            return false
        }

        if (!DateUtil.isPassed(startTime)) {
            Toast.makeText(context, "Attendance Checking has not yet Started", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun checkGeolocation(geoString: String): Boolean {
        val geolocationService = GeolocationService(activity)

        if (!geolocationService.getPermissionAndGPS())
            return false

        val lonLat = geolocationService.getCurrentLonLat()
        if (lonLat == null) {
            Toast.makeText(context, "GPS and Network are unavailable", Toast.LENGTH_LONG).show()
            return false
        }

        val isInLocation = geolocationService.areLocationsEqual(lonLat, geoString)
        if (!isInLocation) {
            Toast.makeText(context, "You are not in the correct location", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private suspend fun checkQr(match: String): Boolean {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE)
            .enableAutoZoom()
            .build()

        val scanner = GmsBarcodeScanning.getClient(activity.applicationContext, options)

        val scanResult = scanner.startScan().await()
        if (scanResult.rawValue.toString() != match) {
            Toast.makeText(context, "QR Code Incorrect", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun createPatternLock(callback: (patternString: String) -> Unit) {
        var patternString: String? = null

        // Pattern Lock
        val patternView = activity.layoutInflater.inflate(R.layout.create_attendance_pattern_lock_layout, null)
        val mPatternLockView: PatternLockView = patternView.findViewById(R.id.pattern_lock_view)
        val mPatternLockViewListener: PatternLockViewListener = object : PatternLockViewListener {
            override fun onStarted() {
                Log.d(javaClass.name, "Pattern drawing started")
            }

            override fun onProgress(progressPattern: List<PatternLockView.Dot>) {
                Log.d(
                    javaClass.name, "Pattern progress: " +
                            PatternLockUtils.patternToString(mPatternLockView, progressPattern)
                )
            }

            override fun onComplete(pattern: List<PatternLockView.Dot>) {
                patternString = PatternLockUtils.patternToString(mPatternLockView, pattern)
                Log.d(
                    javaClass.name, "Pattern complete: " +
                            patternString
                )
            }

            override fun onCleared() {
                Log.d(javaClass.name, "Pattern has been cleared")
            }
        }
        mPatternLockView.addPatternLockListener(mPatternLockViewListener)
        //modal for pattern lock
        val dialog = MaterialAlertDialogBuilder(context).setView(patternView)
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Submit") { dialog, which ->
                callback(patternString!!)
                dialog.cancel()
            }
        val patternModal = dialog.create()
        patternModal.show()
    }

    fun updateAttendanceStatus(courseCode: String, eventTime: String, status: String) {
        runBlocking {
            launch {
                firestoreAttendanceHelper.updateAttendance(
                    courseCode = courseCode,
                    studentEmail = Firebase.auth.currentUser?.email!!,
                    eventTime = eventTime,
                    status = status,
                    onSuccessListener = {
                        Toast.makeText(context, "Attendance Checked", Toast.LENGTH_LONG).show()
                    },
                    onFailureListener = {
                        Toast.makeText(context, "Fail to Check Attendance", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    fun isLate(lateTime: String): Boolean {
        return DateUtil.isPassed(lateTime)
    }

    suspend fun checkAttendanceIsChecked(courseCode: String, eventId: String, email: String): Boolean {
        val event = firestoreAttendanceHelper.getAttendanceByEmailAndEventId(courseCode, eventId, email)!!
        val status = event.get(FirestoreAttendanceHelper.STATUS_FIELD)
        if (status != "ABSENT") {
            Toast.makeText(context, "You have Checked the Attendance", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    suspend fun checkAttendance(code: String? = null, id: String? = null) {
        var courseCode = code
        var eventId = id
        if (courseCode == null) {
            val sp = context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
            courseCode = sp.getString("COURSE_CODE", "")
        }

        var event: MutableMap<String, Any>? = null
        if (eventId != null) {
            event = firestoreEventHelper.getEventFromId(courseCode!!, eventId)
            if (event == null) {
                Toast.makeText(context, "No Attendance to Check", Toast.LENGTH_LONG).show()
                return
            }
        } else {
            val result = firestoreEventHelper.getIncomingEvent(courseCode!!)
            if (result == null) {
                Toast.makeText(context, "No Attendance to Check", Toast.LENGTH_LONG).show()
                return
            } else {
                eventId = result.first
                event = result.second
            }
        }

        val absentTime = event.get(FirestoreEventHelper.ABSENT_TIME) as String
        val startTime = event.get(FirestoreEventHelper.START_TIME) as String
        if (!checkAttendanceIsOpen(absentTime, startTime)) return

        if (!checkAttendanceIsChecked(courseCode, eventId, Firebase.auth.currentUser?.email!!)) return

        val geolocation = event.get(FirestoreEventHelper.GEOLOCATION).toString()
        if (geolocation.isNotEmpty()) {
            if(!checkGeolocation(geolocation)) return
        }

        val useQr = event.get(FirestoreEventHelper.QR) as Boolean
        if (useQr && (id == null)) {
            if(!checkQr(eventId)) return
        }

        val pattern = event.get(FirestoreEventHelper.PATTERN) as String?
        val lateTime = event.get(FirestoreEventHelper.LATE_TIME) as String
        if (pattern != null) {
                createPatternLock {
                    patternString ->
                Log.d("PATTERN", patternString)
                if (patternString != event.get(FirestoreEventHelper.PATTERN))
                    Toast.makeText(context, "Pattern Incorrect", Toast.LENGTH_LONG).show()
                else {
                    if (isLate(lateTime)) {
                        updateAttendanceStatus(courseCode, startTime, "LATE")
                    } else {
                        updateAttendanceStatus(courseCode, startTime, "PRESENT")
                    }
                }

            }
        } else {
            if (isLate(lateTime)) {
                updateAttendanceStatus(courseCode, startTime, "LATE")
            } else {
                updateAttendanceStatus(courseCode, startTime, "PRESENT")
            }
        }
    }
}