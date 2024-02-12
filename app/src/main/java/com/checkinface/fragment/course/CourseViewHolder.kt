package com.checkinface.fragment.course

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.R
import com.checkinface.util.DateUtil

class CourseViewHolder(itemView: View): ViewHolder(itemView) {
    private val tvCourseDate: TextView = itemView.findViewById(R.id.tv_attendance_date)
    private val tvAttendanceStatus: TextView = itemView.findViewById(R.id.tv_attendance_status)
    private val ivAttendanceIconStatus: ImageView = itemView.findViewById(R.id.iv_attendance_icon_status)

    fun bindData(courseModel: CourseModel) {
        val status = courseModel.status.toString()
        tvCourseDate.text = DateUtil.getFormattedDate(courseModel.date)
        tvAttendanceStatus.text = status.lowercase().replaceFirstChar { it.uppercase() }

        when (status) {
            "PRESENT" -> ivAttendanceIconStatus.setImageResource(R.drawable.ic_check)
            "ABSENT" -> ivAttendanceIconStatus.setImageResource(R.drawable.ic_close)
            "LATE" -> ivAttendanceIconStatus.setImageResource(R.drawable.ic_clock)
        }
    }

}