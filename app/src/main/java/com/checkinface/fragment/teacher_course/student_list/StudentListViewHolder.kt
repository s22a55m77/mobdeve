package com.checkinface.fragment.teacher_course.student_list

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.databinding.StudentItemLayoutBinding

class StudentListViewHolder(private val binding: StudentItemLayoutBinding): ViewHolder(binding.root) {
    fun bindData(studentModel: StudentModel) {
        binding.tvStudentName.text = studentModel.name
        binding.tvStudentPresent.text = studentModel.presentCount.toString()
        binding.tvStudentAbsent.text = studentModel.absentCount.toString()
        binding.tvStudentLate.text = studentModel.lateCount.toString()
    }
}