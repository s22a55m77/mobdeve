package com.checkinface.fragment.teacher_course.attendance_list

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.databinding.AttendanceItemLayoutBinding
import com.checkinface.util.DateUtil

class AttendanceListViewHolder(private val binding: AttendanceItemLayoutBinding): ViewHolder(binding.root) {
    fun bindData(attendanceModel: TeacherAttendanceModel) {
        binding.tvAttendanceDate.text = DateUtil.getFormattedDate("MMM d, yyyy HH:mm", attendanceModel.date)
        binding.tvAttendanceStudentPresent.text = attendanceModel.presentCount.toString()
        binding.tvAttendanceStudentAbsent.text = attendanceModel.absentCount.toString()
        binding.tvAttendanceStudentLate.text = attendanceModel.lateCount.toString()
    }
}