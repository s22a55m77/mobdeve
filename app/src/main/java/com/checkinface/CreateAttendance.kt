package com.checkinface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.util.Pair
import androidx.core.util.component1
import androidx.core.util.component2
import com.checkinface.databinding.ActivityCreateAttendanceBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class SelectedDays (
    val monday: Boolean,
    val tuesday: Boolean,
    val wednesday: Boolean,
    val thursday: Boolean,
    val friday: Boolean,
    val saturday: Boolean,
    val sunday: Boolean,
)

class CreateAttendance : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityCreateAttendanceBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select Date Range")
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


        viewBinding.btnDatePicker.setOnClickListener {
            dateRangePicker.show(supportFragmentManager, dateRangePicker.toString())
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
            }
            else {
                viewBinding.cardViewRecurringDayGroup.visibility = View.GONE
                viewBinding.cardViewRecurringDateGroup.visibility = View.GONE
            }
        }

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

            fun getFormatTimeWithTZ(currentTime:Date):String {
                val timeZoneDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                return timeZoneDate.format(currentTime)
            }

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
                val formattedStartDate = getFormatTimeWithTZ(startDate)
                val formattedEndDate = getFormatTimeWithTZ(endDate)

                val message = "Start Date: $formattedStartDate\nEnd Date: $formattedEndDate"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                val datesInRange = getDatesInRange(startDate, endDate, selectedDay)
                // Do something with the list of Mondays, such as displaying them
                for (date in datesInRange) {
                    val formattedMonday = getFormatTimeWithTZ(date)
                    Log.d("Monday", formattedMonday) // Log the Mondays to Logcat
                }
            }
        }

    }
}