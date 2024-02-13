package com.checkinface.activity.create_attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.checkinface.databinding.CreateAttendanceDateLayoutBinding

class SelectedDateAdapter(private val data: ArrayList<DateModel>): RecyclerView.Adapter<SelectedDateViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedDateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CreateAttendanceDateLayoutBinding.inflate(inflater, parent, false)
        return SelectedDateViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SelectedDateViewHolder, position: Int) {
        holder.bindData(data[position])
    }
}