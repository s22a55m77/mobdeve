package com.checkinface.fragment.course

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.databinding.CouseItemLayoutBinding

class CourseAdapter(private val data: ArrayList<AttendanceModel>): Adapter<CourseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CouseItemLayoutBinding.inflate(inflater, parent, false)

        return CourseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bindData(data[position])
    }

}