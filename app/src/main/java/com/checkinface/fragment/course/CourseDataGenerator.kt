package com.checkinface.fragment.course

import com.checkinface.util.DateUtil

class CourseDataGenerator {
    companion object {
        fun loadData(): ArrayList<CourseModel> {
            val data = ArrayList<CourseModel>()
            data.add(
                CourseModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    CourseStatus.PRESENT
                )
            )
            data.add(
                CourseModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    CourseStatus.ABSENT
                )
            )
            data.add(
                CourseModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    CourseStatus.LATE
                )
            )
            data.add(
                CourseModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    CourseStatus.PRESENT
                )
            )
            data.add(
                CourseModel(
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    CourseStatus.PRESENT
                )
            )
            return data
        }
    }
}