package com.checkinface.fragment.course

import java.text.SimpleDateFormat
import java.util.Locale

class CourseDataGenerator {
    companion object {
        fun loadData(): ArrayList<CourseModel> {
            val data = ArrayList<CourseModel>()
            data.add(
                CourseModel(
                    SimpleDateFormat("24/1/2024", Locale.ENGLISH),
                    CourseStatus.PRESENT
                )
            )
            data.add(
                CourseModel(
                    SimpleDateFormat("26/1/2024", Locale.ENGLISH),
                    CourseStatus.ABSENT
                )
            )
            data.add(
                CourseModel(
                    SimpleDateFormat("28/1/2024", Locale.ENGLISH),
                    CourseStatus.LATE
                )
            )
            data.add(
                CourseModel(
                    SimpleDateFormat("30/1/2024", Locale.ENGLISH),
                    CourseStatus.PRESENT
                )
            )
            data.add(
                CourseModel(
                    SimpleDateFormat("1/2/2024", Locale.ENGLISH),
                    CourseStatus.PRESENT
                )
            )
            return data
        }
    }
}