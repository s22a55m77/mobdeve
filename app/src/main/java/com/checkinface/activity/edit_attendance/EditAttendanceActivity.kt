package com.checkinface.activity.edit_attendance

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.util.Pair
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.checkinface.R
import com.checkinface.activity.AzureMapActivity
import com.checkinface.activity.create_attendance.DateModel
import com.checkinface.activity.create_attendance.SelectedDateAdapter
import com.checkinface.activity.create_attendance.SelectedDays
import com.checkinface.databinding.ActivityEditAttendanceBinding
import com.checkinface.util.DateUtil
import com.checkinface.util.FirestoreAttendanceHelper
import com.checkinface.util.FirestoreEventHelper
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import java.util.Date

class EditAttendanceActivity : AppCompatActivity() {
    private val firestoreEventHelper = FirestoreEventHelper()

    private val azureMapResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // TODO get the lon and lat
        }
    }

    companion object {
        private const val START_TIME = "event_start_time"
        private const val LATE_TIME = "event_late_time"
        private const val ABSENT_TIME = "event_absent_time"
        private const val PATTERN_LOCK = "pattern_lock"
        private const val USE_GEOLOCATION = "use_geolocation"
        private const val USE_QR = "use_qr"
    }

    private fun parsePatternLockString(patternString: String): List<PatternLockView.Dot> {
        val dotList = mutableListOf<PatternLockView.Dot>()

        // Define the pattern for extracting row and column values
        val pattern = Regex("Row = (\\d+), Col = (\\d+)")

        // Find all matches in the input patternString
        pattern.findAll(patternString).forEach { matchResult ->
            // Extract row and column values from the match
            val row = matchResult.groupValues[1].toInt()
            val col = matchResult.groupValues[2].toInt()

            // Create PatternLockView.Dot object and add to the list
            val dot = PatternLockView.Dot.of(row, col)
            dotList.add(dot)
        }

        return dotList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityEditAttendanceBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val sp = viewBinding.root.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
        val courseCode = sp.getString("COURSE_CODE", "")
        val eventTime = sp.getString("EVENT_TIME", "")
        var eventDetail: MutableMap<String, Any>? = mutableMapOf()

        var datePicker: MaterialDatePicker<Long>? = null
        var startTimePicker: MaterialTimePicker? = null
        var lateTimePicker: MaterialTimePicker? = null
        var absentTimePicker: MaterialTimePicker? = null

        val patternView = layoutInflater.inflate(R.layout.create_attendance_pattern_lock_layout, null)
        val mPatternLockView: PatternLockView = patternView.findViewById(R.id.pattern_lock_view)

        // Get Event Detail
        lifecycleScope.launch {
            eventDetail = firestoreEventHelper.getEventDetailBasedOnEvent(courseCode!!, eventTime!!)
        }.invokeOnCompletion {

            // Initialize Edit State
            // CheckBox
            viewBinding.checkboxGeolocation.isChecked = eventDetail?.get(USE_GEOLOCATION).toString().toBoolean()
            if(eventDetail?.get(PATTERN_LOCK) != null)
                viewBinding.checkboxPatternPassword.isChecked = true
            viewBinding.checkboxQrCode.isChecked = eventDetail?.get(USE_QR).toString().toBoolean()

            // Date
            val date = DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(START_TIME).toString(), true)
            var message = "Select Date: " + DateUtil.getFormattedDate(date)
            viewBinding.btnDatePicker.text = message

            datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date Range")
                    .setSelection(date.time)
                    .build()

            // Firestore String to Date to Hour String to Int
            val startTimeHour = DateUtil.getFormattedDate("HH", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                START_TIME).toString())).toInt()
            val startTimeMinute = DateUtil.getFormattedDate("mm", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                START_TIME).toString())).toInt()
            message = "Select Start Time: $startTimeHour:$startTimeMinute"
            viewBinding.btnStartTimePicker.text = message


            startTimePicker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(startTimeHour)
                    .setMinute(startTimeMinute)
                    .setTitleText("Select Attendance Start time")
                    .build()


            val lateTimeHour = DateUtil.getFormattedDate("HH", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                LATE_TIME).toString())).toInt()
            val lateTimeMinute = DateUtil.getFormattedDate("HH", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                LATE_TIME).toString())).toInt()
            message =
                "Select Start Time: $lateTimeHour:$lateTimeMinute"
            viewBinding.btnLateTimePicker.text = message

            lateTimePicker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(lateTimeHour)
                    .setMinute(lateTimeMinute)
                    .setTitleText("Select Attendance Late time")
                    .build()

            val absentTimeHour = DateUtil.getFormattedDate("HH", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                ABSENT_TIME).toString())).toInt()
            val absentTimeMinute = DateUtil.getFormattedDate("HH", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                ABSENT_TIME).toString())).toInt()
            message =
                "Select Start Time: $absentTimeHour:$absentTimeMinute"
            viewBinding.btnAbsentTimePicker.text = message

            absentTimePicker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(absentTimeHour)
                    .setMinute(absentTimeMinute)
                    .setTitleText("Select Attendance Absent time")
                    .build()


            // Material Component Listener
            startTimePicker?.addOnPositiveButtonClickListener {
                val message =
                    "Select Start Time: " + startTimePicker?.hour + ":" + startTimePicker?.minute
                viewBinding.btnStartTimePicker.text = message
            }

            datePicker?.addOnPositiveButtonClickListener {
                val message = "Select Date: " + DateUtil.getFormattedDate(Date(datePicker?.selection!!))
                viewBinding.btnDatePicker.text = message
            }

            lateTimePicker?.addOnPositiveButtonClickListener {
                val message = "Select Late Time: " + lateTimePicker?.hour + ":" + lateTimePicker?.minute
                viewBinding.btnLateTimePicker.text = message
            }

            absentTimePicker?.addOnPositiveButtonClickListener {
                val message =
                    "Select Absent Time: " + absentTimePicker?.hour + ":" + absentTimePicker?.minute
                viewBinding.btnAbsentTimePicker.text = message
            }

            // Pattern Lock
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
                    Log.d(
                        javaClass.name, "Pattern complete: " +
                                PatternLockUtils.patternToString(mPatternLockView, pattern)
                    )
                }

                override fun onCleared() {
                    Log.d(javaClass.name, "Pattern has been cleared")
                }
            }
            mPatternLockView.addPatternLockListener(mPatternLockViewListener)
            mPatternLockView.setPattern(PatternLockView.PatternViewMode.CORRECT, parsePatternLockString(eventDetail?.get(PATTERN_LOCK).toString()))
        }

        // listener binding
        viewBinding.btnDatePicker.setOnClickListener {
            datePicker?.show(supportFragmentManager, datePicker.toString())
        }

        viewBinding.btnStartTimePicker.setOnClickListener {
            startTimePicker?.show(supportFragmentManager, startTimePicker.toString())
        }

        viewBinding.btnLateTimePicker.setOnClickListener {
            lateTimePicker?.show(supportFragmentManager, lateTimePicker.toString())
        }

        viewBinding.btnAbsentTimePicker.setOnClickListener {
            absentTimePicker?.show(supportFragmentManager, absentTimePicker.toString())
        }

        viewBinding.checkboxGeolocation.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewBinding.btnGeolocation.visibility = View.VISIBLE
            } else {
                viewBinding.btnGeolocation.visibility = View.GONE
            }
        }

        viewBinding.btnGeolocation.setOnClickListener {
            val intentToMap = Intent(this@EditAttendanceActivity, AzureMapActivity::class.java)
            azureMapResultLauncher.launch(intentToMap)
        }

        viewBinding.checkboxPatternPassword.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewBinding.btnPattern.visibility = View.VISIBLE
            } else {
                viewBinding.btnPattern.visibility = View.GONE
            }
        }

        val dialog = MaterialAlertDialogBuilder(this).setView(patternView)
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Submit") { dialog, which ->
                dialog.cancel()
            }
        val patternModal = dialog.create()
        viewBinding.btnPattern.setOnClickListener {
            patternModal.show()
        }

        // Handle Save
        viewBinding.btnEditAttendance.setOnClickListener {
            val selectedDateInMillis = datePicker?.selection ?: 0

            var startTimeHourMillis: Int
            var startTimeMinuteMillis: Int
            var lateTimeHourMillis: Int
            var lateTimeMinuteMillis: Int
            var absentTimeHourMillis: Int
            var absentTimeMinuteMillis: Int

            try {
                startTimeHourMillis = (startTimePicker?.hour ?: 0) * 3600000
                startTimeMinuteMillis = (startTimePicker?.minute ?: 0) * 60000
            } catch (e: Exception) {
                startTimeHourMillis = DateUtil.getFormattedDate("HH", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                    START_TIME).toString())).toInt() * 3600000
                startTimeMinuteMillis = DateUtil.getFormattedDate("mm", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                    START_TIME).toString())).toInt() * 60000
            }

            try {
                lateTimeHourMillis = (lateTimePicker?.hour ?: 0) * 3600000
                lateTimeMinuteMillis = (lateTimePicker?.minute ?: 0) * 60000
            } catch (e: Exception) {
                lateTimeHourMillis = DateUtil.getFormattedDate("HH", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                    LATE_TIME).toString())).toInt() * 3600000
                lateTimeMinuteMillis = DateUtil.getFormattedDate("mm", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                    LATE_TIME).toString())).toInt() * 60000
            }

            try {
                absentTimeHourMillis = (absentTimePicker?.hour ?: 0) * 3600000
                absentTimeMinuteMillis = (absentTimePicker?.minute ?: 0) * 60000
            } catch (e: Exception) {
                absentTimeHourMillis = DateUtil.getFormattedDate("HH", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                    ABSENT_TIME).toString())).toInt() * 3600000
                absentTimeMinuteMillis = DateUtil.getFormattedDate("mm", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                    ABSENT_TIME).toString())).toInt() * 60000
            }

            val startTime = DateUtil.millisecondsToTimestamp(selectedDateInMillis + startTimeHourMillis + startTimeMinuteMillis)
            val lateTime = DateUtil.millisecondsToTimestamp(selectedDateInMillis + lateTimeHourMillis + lateTimeMinuteMillis)
            val absentTime = DateUtil.millisecondsToTimestamp(selectedDateInMillis + absentTimeHourMillis + absentTimeMinuteMillis)
            lifecycleScope.launch {
                firestoreEventHelper.updateEvent(
                    courseCode = courseCode!!,
                    origEventTime = eventTime!!,
                    startTime = startTime,
                    lateTime = lateTime,
                    absentTime = absentTime,
                    useGeolocation = viewBinding.checkboxGeolocation.isChecked,
                    patternLock = if (mPatternLockView.pattern.size == 0) null else mPatternLockView.pattern.toString(),
                    useQR = viewBinding.checkboxQrCode.isChecked,
                    onSuccessListener = fun() {
                        Toast.makeText(viewBinding.root.context, "Saved", Toast.LENGTH_LONG).show()
                        finish()
                    }
                )
            }

        }

        // Handle Delete
        viewBinding.btnDeleteAttendance.setOnClickListener {
            lifecycleScope.launch {
                firestoreEventHelper.deleteEvent(courseCode!!, eventTime!!,
                    fun() {
                        Toast.makeText(viewBinding.root.context, "Deleted", Toast.LENGTH_LONG).show()
                        finish()
                    })
            }
        }
    }
}