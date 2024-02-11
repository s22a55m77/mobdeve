package com.checkinface.fragment.course

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.R
import java.util.Date

class CourseViewHolder(itemView: View): ViewHolder(itemView) {
    private val tvCourseDate: TextView = itemView.findViewById(R.id.tv_attendance_date)
    private val tvAttendanceStatus: TextView = itemView.findViewById(R.id.tv_attendance_status)
    private val ivAttendanceIconStatus: ImageView = itemView.findViewById(R.id.iv_attendance_icon_status)

    fun bindData(courseModel: CourseModel) {
        val status = courseModel.status.toString()
        tvCourseDate.text = courseModel.date.format(Date(System.currentTimeMillis()))
        tvAttendanceStatus.text = status.lowercase().replaceFirstChar { it.uppercase() }

        if (status == "PRESENT")
            ivAttendanceIconStatus.setImageResource(R.drawable.ic_check)
        else if (status == "ABSENT")
            ivAttendanceIconStatus.setImageResource(R.drawable.ic_close)
        else if (status == "LATE")
            ivAttendanceIconStatus.setImageResource(R.drawable.ic_clock)
    }

}