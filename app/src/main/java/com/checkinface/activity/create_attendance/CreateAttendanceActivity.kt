package com.checkinface.activity.create_attendance

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.PatternLockView.Dot
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.checkinface.R
import com.checkinface.databinding.ActivityCreateAttendanceBinding
import com.checkinface.util.DateUtil
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar
import java.util.Date


class CreateAttendanceActivity : AppCompatActivity() {
    private lateinit var selectedDateRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityCreateAttendanceBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // initialize date picker
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select Date Range")
                .build()

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date Range")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        val startTimePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTitleText("Select Attendance Start time")
                .build()

        val lateTimePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTitleText("Select Attendance Late time")
                .build()

        val absentTimePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTitleText("Select Attendance Absent time")
                .build()


        // listener binding
        viewBinding.btnDateRangePicker.setOnClickListener {
            dateRangePicker.show(supportFragmentManager, dateRangePicker.toString())
        }

        viewBinding.btnDatePicker.setOnClickListener {
            datePicker.show(supportFragmentManager, datePicker.toString())
        }

        viewBinding.btnStartTimePicker.setOnClickListener {
            startTimePicker.show(supportFragmentManager, startTimePicker.toString())
        }

        viewBinding.btnLateTimePicker.setOnClickListener {
            lateTimePicker.show(supportFragmentManager, lateTimePicker.toString())
        }

        viewBinding.btnAbsentTimePicker.setOnClickListener {
            absentTimePicker.show(supportFragmentManager, absentTimePicker.toString())
        }

        viewBinding.checkboxGeolocation.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                viewBinding.btnGeolocation.visibility = View.VISIBLE
            }
            else {
                viewBinding.btnGeolocation.visibility = View.GONE
            }
        }

        viewBinding.checkboxPatternPassword.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                viewBinding.btnPattern.visibility = View.VISIBLE
            }
            else {
                viewBinding.btnPattern.visibility = View.GONE
            }
        }

        viewBinding.checkboxRecurring.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                viewBinding.cardViewRecurringDayGroup.visibility = View.VISIBLE
                viewBinding.cardViewRecurringDateGroup.visibility = View.VISIBLE
                viewBinding.cardViewRecurringSelectedDateGroup.visibility = View.VISIBLE
                viewBinding.cardViewDateGroup.visibility = View.GONE
            }
            else {
                viewBinding.cardViewDateGroup.visibility = View.VISIBLE
                viewBinding.cardViewRecurringDayGroup.visibility = View.GONE
                viewBinding.cardViewRecurringDateGroup.visibility = View.GONE
                viewBinding.cardViewRecurringSelectedDateGroup.visibility = View.GONE
            }
        }

        // handle checkbox select for recurring event
        dateRangePicker.addOnPositiveButtonClickListener {
            val selectedDay = SelectedDays(
                monday = viewBinding.checkboxMonday.isChecked,
                tuesday = viewBinding.checkboxTuesday.isChecked,
                wednesday = viewBinding.checkboxWednesday.isChecked,
                thursday = viewBinding.checkboxThursday.isChecked,
                friday = viewBinding.checkboxFriday.isChecked,
                saturday = viewBinding.checkboxSaturday.isChecked,
                sunday = viewBinding.checkboxSunday.isChecked,
            )
            val date = dateRangePicker.selection

            fun getDatesInRange(startDate: Date, endDate: Date, days: SelectedDays): List<Date> {
                val dates = mutableListOf<Date>()
                val calendar = Calendar.getInstance()
                calendar.time = startDate
                while (!calendar.time.after(endDate)) {
                    if (days.monday && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                        dates.add(calendar.time)
                    }
                    if (days.tuesday && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                        dates.add(calendar.time)
                    }
                    if (days.wednesday && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                        dates.add(calendar.time)
                    }
                    if (days.thursday && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                        dates.add(calendar.time)
                    }
                    if (days.friday && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                        dates.add(calendar.time)
                    }
                    if (days.saturday && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                        dates.add(calendar.time)
                    }
                    if (days.sunday && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        dates.add(calendar.time)
                    }
                    calendar.add(Calendar.DATE, 1)
                }
                return dates
            }

            if (date != null) {
                val startDate = Date(date.first)
                val endDate = Date(date.second)
                val formattedStartDate = DateUtil.getFormattedDate(startDate)
                val formattedEndDate = DateUtil.getFormattedDate(endDate)

                selectedDateRecyclerView = viewBinding.rvSelectedDate

                val linearLayoutManager = LinearLayoutManager(applicationContext)
                selectedDateRecyclerView.layoutManager = linearLayoutManager

                val message = "Start Date: $formattedStartDate\nEnd Date: $formattedEndDate"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                val datesInRange = getDatesInRange(startDate, endDate, selectedDay)
                val dateModelList = ArrayList<DateModel>()
                for (date in datesInRange) {
                    val model = DateModel(DateUtil.getFormattedDate(date))
                    dateModelList.add(model)
                }

                selectedDateRecyclerView.adapter = SelectedDateAdapter(dateModelList)
            }
        }

        datePicker.addOnPositiveButtonClickListener {
            val message = "Select Date: " + DateUtil.getFormattedDate(Date(datePicker.selection!!))
            viewBinding.btnDatePicker.text = message
        }

        startTimePicker.addOnPositiveButtonClickListener {
            val message = "Select Start Time: " + startTimePicker.hour + ":" + startTimePicker.minute
            viewBinding.btnStartTimePicker.text = message
        }

        lateTimePicker.addOnPositiveButtonClickListener {
            val message = "Select Late Time: " + lateTimePicker.hour + ":" + lateTimePicker.minute
            viewBinding.btnLateTimePicker.text = message
        }

        absentTimePicker.addOnPositiveButtonClickListener {
            val message = "Select Absent Time: " + absentTimePicker.hour + ":" + absentTimePicker.minute
            viewBinding.btnAbsentTimePicker.text = message
        }

        // Pattern Lock

        val patternView = layoutInflater.inflate(R.layout.create_attendance_pattern_lock_layout, null)
        val mPatternLockView: PatternLockView = patternView.findViewById(R.id.pattern_lock_view)
        val mPatternLockViewListener: PatternLockViewListener = object : PatternLockViewListener {
            override fun onStarted() {
                Log.d(javaClass.name, "Pattern drawing started")
            }

            override fun onProgress(progressPattern: List<Dot>) {
                Log.d(
                    javaClass.name, "Pattern progress: " +
                            PatternLockUtils.patternToString(mPatternLockView, progressPattern)
                )
            }

            override fun onComplete(pattern: List<Dot>) {
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

    }
}