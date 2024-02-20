package com.checkinface.fragment.student_attendance_list

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.R
import com.checkinface.databinding.StudentAttendanceItemLayoutBinding
import com.checkinface.util.DateUtil

class StudentAttendanceListViewHolder(private val binding: StudentAttendanceItemLayoutBinding): ViewHolder(binding.root) {

    fun bindData(studentAttendanceModel: StudentAttendanceModel) {
        val status = studentAttendanceModel.status.toString()
        binding.tvAttendanceDate.text = DateUtil.getFormattedDate(studentAttendanceModel.date)
        binding.tvAttendanceStatus.text = status.lowercase().replaceFirstChar { it.uppercase() }

        when (status) {
            "PRESENT" -> binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_check)
            "ABSENT" -> binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_close)
            "LATE" -> binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_clock)
        }
    }

}