package com.checkinface.fragment.teacher_course.student_list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.R
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
        val bundle = Bundle()
        bundle.putString("studentEmail", data[position].email)
        holder.itemView.setOnClickListener {
            // Put student email into SP for the detail page usage
            val sp = holder.itemView.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
            with(sp.edit()) {
                putString("STUDENT_EMAIL", data[position].email)
                apply()
            }
            // Navigate to detail page
            val navController = holder.itemView.findNavController()
            navController.navigate(R.id.action_student_list_to_student_attendance, bundle)
        }
    }
}