package com.checkinface.fragment.teacher_course.attendance_detail_student_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.R
import com.checkinface.databinding.AttendanceDetailStudentItemLayoutBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AttendanceDetailStudentListAdapter(private val data: ArrayList<AttendanceDetailStudentModel>): Adapter<AttendanceDetailStudentListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AttendanceDetailStudentListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AttendanceDetailStudentItemLayoutBinding.inflate(inflater, parent, false)

        return AttendanceDetailStudentListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AttendanceDetailStudentListViewHolder, position: Int) {
        holder.bindData(data[position])

        holder.itemView.setOnClickListener {
            val layoutInflater = LayoutInflater.from(holder.itemView.context)
            val modalView = layoutInflater.inflate(R.layout.modify_student_attendance_modal_layout, null)
            val modal = MaterialAlertDialogBuilder(holder.itemView.context).setView(modalView)
            modal.show()

        }
    }
}