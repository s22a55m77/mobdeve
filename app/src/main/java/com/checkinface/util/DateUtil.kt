package com.checkinface.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DateUtil {
    companion object {
        private val inputDateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX"
        private val outputDateFormat = "MMM d, yyyy"

        fun getDate(date: String): Date {
            val dateFormat = SimpleDateFormat(inputDateFormat, Locale.getDefault())
            return dateFormat.parse(date)!!
        }

        fun getDate(inputDateFormat: String, date: String): Date {
            val dateFormat = SimpleDateFormat(inputDateFormat, Locale.getDefault())
            return dateFormat.parse(date)!!
        }

        fun getDate(inputDateFormat: String, date: String, toUTC0: Boolean): Date {
            val dateFormat = SimpleDateFormat(inputDateFormat, Locale.getDefault())
            if(toUTC0)
                dateFormat.timeZone = TimeZone.getTimeZone("GMT+0")
            return dateFormat.parse(date)!!
        }

        fun getFormattedDate(date: Date): String {
            val dateFormat = SimpleDateFormat(outputDateFormat, Locale.ENGLISH)
            return dateFormat.format(date)
        }

        fun getFormattedDate(outputDateFormat: String, date: Date): String {
            val dateFormat = SimpleDateFormat(outputDateFormat, Locale.ENGLISH)
            return dateFormat.format(date)
        }

        fun millisecondsToTimestamp(millis: Long): String {
            // Create a SimpleDateFormat object with the desired date format
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            // Set the time zone to UTC+8
            dateFormat.timeZone = TimeZone.getTimeZone("UTC+8")

            // Convert milliseconds to a Date object
            val date = Date(millis)

            // Format the Date object to a string and return it
            return dateFormat.format(date)
        }

        fun getCurrentDate(): String {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            return dateFormat.format(calendar.time)
        }

        fun isPassed(time: String): Boolean {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = dateFormat.parse(time)
            val currentDate = Date()

            return date?.time ?: 0 < currentDate.time
        }

    }


}