package com.checkinface.fragment.course

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.R
import com.checkinface.databinding.CouseItemLayoutBinding
import com.checkinface.util.DateUtil

class CourseViewHolder(private val binding: CouseItemLayoutBinding): ViewHolder(binding.root) {

    fun bindData(attendanceModel: AttendanceModel) {
        val status = attendanceModel.status.toString()
        binding.tvAttendanceDate.text = DateUtil.getFormattedDate(attendanceModel.date)
        binding.tvAttendanceStatus.text = status.lowercase().replaceFirstChar { it.uppercase() }

        when (status) {
            "PRESENT" -> binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_check)
            "ABSENT" -> binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_close)
            "LATE" -> binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_clock)
        }
    }

}