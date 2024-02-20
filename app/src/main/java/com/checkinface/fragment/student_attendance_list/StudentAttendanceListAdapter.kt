package com.checkinface.fragment.student_attendance_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.R
import com.checkinface.databinding.CouseItemLayoutBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class StudentAttendanceListAdapter(private val data: ArrayList<StudentAttendanceModel>): Adapter<StudentAttendanceListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentAttendanceListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CouseItemLayoutBinding.inflate(inflater, parent, false)

        return StudentAttendanceListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: StudentAttendanceListViewHolder, position: Int) {
        holder.bindData(data[position])

        holder.itemView.setOnClickListener {
            val layoutInflater = LayoutInflater.from(holder.itemView.context)
            val modalView = layoutInflater.inflate(R.layout.modify_student_attendance_modal_layout, null)
            val modal = MaterialAlertDialogBuilder(holder.itemView.context).setView(modalView)
            modal.show()
        }
    }

}