package com.checkinface.fragment.teacher_course.student_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.R
import com.checkinface.databinding.StudentItemLayoutBinding
import com.checkinface.util.VariableHolder


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

        // Handle null ID
        if (data[position].id == null) {
            val tvId = holder.itemView.findViewById<TextView>(R.id.tv_student_id)
            tvId.visibility = TextView.GONE
        }

        // Handle click on student
        holder.itemView.setOnClickListener {
            // Put student email into SP for the detail page usage
//            val sp = holder.itemView.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
//            with(sp.edit()) {
//                putString("STUDENT_EMAIL", data[position].email)
//                apply()
//            }
            VariableHolder.getInstance().studentEmail = data[position].email
            // Navigate to detail page
            val navController = holder.itemView.findNavController()
            navController.navigate(R.id.action_student_list_to_student_attendance, bundle)
        }
    }
}