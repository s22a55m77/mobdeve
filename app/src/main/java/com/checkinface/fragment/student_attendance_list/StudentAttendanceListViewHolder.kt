package com.checkinface.fragment.student_attendance_list

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.R
import com.checkinface.databinding.StudentAttendanceItemLayoutBinding
import com.checkinface.util.DateUtil
import java.util.Date

class StudentAttendanceListViewHolder(private val binding: StudentAttendanceItemLayoutBinding): ViewHolder(binding.root) {

    fun bindData(studentAttendanceModel: StudentAttendanceModel) {
        val status = studentAttendanceModel.status.toString()
        binding.tvAttendanceDate.text = DateUtil.getFormattedDate("MMM d, yyyy HH:mm", studentAttendanceModel.date)
        binding.tvAttendanceStatus.text = status.lowercase().replaceFirstChar { it.uppercase() }

        if (studentAttendanceModel.date.time < Date().time) {
            when (status) {
                "PRESENT" -> binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_check)
                "ABSENT" -> binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_close)
                "LATE" -> binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_clock)
            }
        }
        else {
            binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_pending)
            binding.tvAttendanceStatus.text = "Pending"
        }


    }

}