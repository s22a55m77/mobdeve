package com.checkinface.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.checkinface.R
import com.checkinface.databinding.ActivityEditAttendanceBinding
import com.checkinface.util.DateUtil
import com.checkinface.util.FirestoreEventHelper
import com.checkinface.util.GeolocationService
import com.checkinface.util.VariableHolder
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import java.util.Date

class EditAttendanceActivity : AppCompatActivity() {
    private val firestoreEventHelper = FirestoreEventHelper()
    private var geolocation: String? = null
    private var patternString: String? = null
    private lateinit var viewBinding: ActivityEditAttendanceBinding

    private val azureMapResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val lon = result.data?.getStringExtra(AzureMapActivity.LON_KEY)!!
            val lat = result.data?.getStringExtra(AzureMapActivity.LAT_KEY)!!
            geolocation = "$lon $lat"
            viewBinding.llGeolocationText.visibility = LinearLayout.VISIBLE
            viewBinding.tvGeolocation.text = geolocation
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityEditAttendanceBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

//        val sp = viewBinding.root.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
//        val courseCode = sp.getString("COURSE_CODE", "")
//        val eventTime = sp.getString("EVENT_TIME", "")
        val courseCode = VariableHolder.getInstance().courseCode
        val eventTime = VariableHolder.getInstance().eventTime
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
            viewBinding.checkboxGeolocation.isChecked = eventDetail?.get(FirestoreEventHelper.GEOLOCATION) != null
            if(viewBinding.checkboxGeolocation.isChecked) {
                geolocation = eventDetail?.get(FirestoreEventHelper.GEOLOCATION).toString()
                viewBinding.tvGeolocation.text = geolocation
                viewBinding.llGeolocationText.visibility = LinearLayout.VISIBLE
            }

            if(eventDetail?.get(FirestoreEventHelper.PATTERN) != null)
                viewBinding.checkboxPatternPassword.isChecked = true
            viewBinding.checkboxQrCode.isChecked = eventDetail?.get(FirestoreEventHelper.QR).toString().toBoolean()

            // Date
            val date = DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(FirestoreEventHelper.START_TIME).toString(), true)
            var message = "Select Date: " + DateUtil.getFormattedDate(date)
            viewBinding.btnDatePicker.text = message

            datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date Range")
                    .setSelection(date.time)
                    .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                    .setCalendarConstraints(
                        CalendarConstraints.Builder()
                            .setValidator(DateValidatorPointForward.now())
                            .build()
                    )
                    .build()

            // Firestore String to Date to Hour String to Int
            val startTimeHour = DateUtil.getFormattedDate("HH", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                FirestoreEventHelper.START_TIME).toString())).toInt()
            val startTimeMinute = DateUtil.getFormattedDate("mm", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                FirestoreEventHelper.START_TIME).toString())).toInt()
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
                FirestoreEventHelper.LATE_TIME).toString())).toInt()
            val lateTimeMinute = DateUtil.getFormattedDate("mm", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                FirestoreEventHelper.LATE_TIME).toString())).toInt()
            message =
                "Select Late Time: $lateTimeHour:$lateTimeMinute"
            viewBinding.btnLateTimePicker.text = message

            lateTimePicker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(lateTimeHour)
                    .setMinute(lateTimeMinute)
                    .setTitleText("Select Attendance Late time")
                    .build()

            val absentTimeHour = DateUtil.getFormattedDate("HH", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                FirestoreEventHelper.ABSENT_TIME).toString())).toInt()
            val absentTimeMinute = DateUtil.getFormattedDate("mm", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                FirestoreEventHelper.ABSENT_TIME).toString())).toInt()
            message =
                "Select Absent Time: $absentTimeHour:$absentTimeMinute"
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
                    patternString = PatternLockUtils.patternToString(mPatternLockView, pattern)
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
            patternString = eventDetail?.get(FirestoreEventHelper.PATTERN).toString()
            if(patternString != "null") {
                val pattern = PatternLockUtils.stringToPattern(mPatternLockView, patternString)
                mPatternLockView.setPattern(PatternLockView.PatternViewMode.CORRECT, pattern)
            }
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
                viewBinding.llGeolocationGroup.visibility = View.VISIBLE
            } else {
                viewBinding.llGeolocationGroup.visibility = View.GONE
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

        viewBinding.btnUseCurrentGeolocation.setOnClickListener {
            val geolocationService = GeolocationService(this@EditAttendanceActivity)
            if(!geolocationService.getPermissionAndGPS()) {
                Toast.makeText(applicationContext, "Please enable GPS", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            geolocation = geolocationService.getCurrentLonLat()
            viewBinding.llGeolocationText.visibility = LinearLayout.VISIBLE
            viewBinding.tvGeolocation.text = geolocation
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
                    FirestoreEventHelper.START_TIME).toString())).toInt() * 3600000
                startTimeMinuteMillis = DateUtil.getFormattedDate("mm", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                    FirestoreEventHelper.START_TIME).toString())).toInt() * 60000
            }

            try {
                lateTimeHourMillis = (lateTimePicker?.hour ?: 0) * 3600000
                lateTimeMinuteMillis = (lateTimePicker?.minute ?: 0) * 60000
            } catch (e: Exception) {
                lateTimeHourMillis = DateUtil.getFormattedDate("HH", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                    FirestoreEventHelper.LATE_TIME).toString())).toInt() * 3600000
                lateTimeMinuteMillis = DateUtil.getFormattedDate("mm", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                    FirestoreEventHelper.LATE_TIME).toString())).toInt() * 60000
            }

            try {
                absentTimeHourMillis = (absentTimePicker?.hour ?: 0) * 3600000
                absentTimeMinuteMillis = (absentTimePicker?.minute ?: 0) * 60000
            } catch (e: Exception) {
                absentTimeHourMillis = DateUtil.getFormattedDate("HH", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                    FirestoreEventHelper.ABSENT_TIME).toString())).toInt() * 3600000
                absentTimeMinuteMillis = DateUtil.getFormattedDate("mm", DateUtil.getDate("yyyy-MM-dd HH:mm:ss", eventDetail?.get(
                    FirestoreEventHelper.ABSENT_TIME).toString())).toInt() * 60000
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
                    geolocation = geolocation,
                    patternLock = patternString,
                    useQR = viewBinding.checkboxQrCode.isChecked,
                    onSuccessListener = fun() {
                        Toast.makeText(viewBinding.root.context, "Saved", Toast.LENGTH_LONG).show()
                        finish()
                    },
                    onFailureListener = fun(e) {
                        Toast.makeText(viewBinding.root.context, "Error: ${e.message.toString()}", Toast.LENGTH_LONG).show()
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
                    },
                    fun(e) {
                        Toast.makeText(viewBinding.root.context, "Error: ${e.message.toString()}", Toast.LENGTH_LONG).show()
                    })
            }
        }
    }
}