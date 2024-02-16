package com.checkinface.fragment.teacher_course

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.databinding.StudentItemLayoutBinding

class StudentListViewHolder(private val binding: StudentItemLayoutBinding): ViewHolder(binding.root) {
    fun bindData(studentModel: StudentModel) {
        binding.tvStudentName.text = studentModel.name
        binding.tvStudentPresent.text = "Present: ${studentModel.presentCount}"
        binding.tvStudentAbsent.text = "Absent: ${studentModel.absentCount}"
        binding.tvStudentLate.text = "Late: ${studentModel.lateCount}"
    }
}