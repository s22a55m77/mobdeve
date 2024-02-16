package com.checkinface.fragment.teacher_course.attendance_detail_student_list

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.R
import com.checkinface.databinding.AttendanceDetailStudentItemLayoutBinding

class AttendanceDetailStudentListViewHolder(private val binding: AttendanceDetailStudentItemLayoutBinding): ViewHolder(binding.root) {
    fun bindData(attendanceDetailStudentModel: AttendanceDetailStudentModel) {
        binding.tvAttendanceDetailStudentName.text = attendanceDetailStudentModel.name
        val status = attendanceDetailStudentModel.status.toString()
        binding.tvAttendanceDetailStudentStatus.text = status.lowercase().replaceFirstChar { it.uppercase() }

        when (status) {
            "PRESENT" -> binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_check)
            "ABSENT" -> binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_close)
            "LATE" -> binding.ivAttendanceIconStatus.setImageResource(R.drawable.ic_clock)
        }
    }
}