package com.checkinface.fragment.teacher_course.attendance_list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.R
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

        // details
        holder.itemView.setOnClickListener {
            val navController = holder.itemView.findNavController()
            navController.navigate(R.id.action_attendance_list_to_detail)
        }

        val settings = holder.itemView.findViewById<ImageView>(R.id.iv_attendance_settings)
        settings.setOnClickListener {

        }

        val qrcode = holder.itemView.findViewById<ImageView>(R.id.iv_attendance_item_qr_code)
        qrcode.setOnClickListener {
            
        }
    }
}