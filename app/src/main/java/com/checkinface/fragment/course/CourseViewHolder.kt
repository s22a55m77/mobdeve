package com.checkinface.fragment.course

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.R
import com.checkinface.databinding.CouseItemLayoutBinding
import com.checkinface.util.DateUtil

class CourseViewHolder(private val binding: CouseItemLayoutBinding): ViewHolder(binding.root) {

    fun bindData(courseModel: CourseModel) {
        val status = courseModel.status.toString()
        binding.tvAttendanceDate.text = DateUtil.getFormattedDate(courseModel.date)
        binding.tvAttendanceStatus.text = status.lowercase().replaceFirstChar { it.uppercase() }

        when (status) {
            "PRESENT" -> binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_check)
            "ABSENT" -> binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_close)
            "LATE" -> binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_clock)
        }
    }

}