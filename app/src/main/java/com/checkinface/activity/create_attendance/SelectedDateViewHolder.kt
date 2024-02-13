package com.checkinface.activity.create_attendance

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.databinding.CreateAttendanceDateLayoutBinding

class SelectedDateViewHolder(private val binding: CreateAttendanceDateLayoutBinding): ViewHolder(binding.root) {
    fun bindData(dateModel: DateModel) {
        binding.tvDate.text = dateModel.date
    }
}