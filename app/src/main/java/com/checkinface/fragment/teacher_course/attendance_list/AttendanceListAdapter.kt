package com.checkinface.fragment.teacher_course.attendance_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.databinding.AttendanceItemLayoutBinding

class AttendanceListAdapter(private val data: ArrayList<TeacherAttendanceModel>): Adapter<AttendanceListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AttendanceItemLayoutBinding.inflate(inflater, parent, false)

        return AttendanceListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AttendanceListViewHolder, position: Int) {
        holder.bindData(data[position])
    }
}