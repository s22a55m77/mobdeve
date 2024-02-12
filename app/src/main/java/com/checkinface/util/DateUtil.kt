package com.checkinface.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateUtil {
    companion object {
        private val inputDateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX"
        private val outputDateFormat = "MMM d, yyyy"

        fun getDate(date: String): Date {
            val dateFormat = SimpleDateFormat(inputDateFormat, Locale.getDefault())
            return dateFormat.parse(date)!!
        }

        fun getFormattedDate(date: Date): String {
            val dateFormat = SimpleDateFormat(outputDateFormat, Locale.ENGLISH)
            return dateFormat.format(date)
        }
    }


}