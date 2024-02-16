package com.checkinface.fragment.teacher_course

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.databinding.StudentItemLayoutBinding

class StudentListAdapter(private val data: ArrayList<StudentModel>): Adapter<StudentListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StudentItemLayoutBinding.inflate(inflater, parent, false)

        return StudentListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: StudentListViewHolder, position: Int) {
        holder.bindData(data[position])
    }
}