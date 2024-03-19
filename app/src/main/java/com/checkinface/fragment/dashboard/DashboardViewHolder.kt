package com.checkinface.fragment.dashboard

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.databinding.DashboardItemLayoutBinding
import com.checkinface.util.DateUtil

class DashboardViewHolder(private val binding: DashboardItemLayoutBinding): ViewHolder(binding.root) {
    fun bindData(dashboardModel: DashboardModel) {
        binding.tvDashboardCourse.text = dashboardModel.course
        if (dashboardModel.nextCheckTime !== null) {
            binding.tvDashboardCheckTime.text = DateUtil.getFormattedDate("MMM d, yyyy HH:mm", dashboardModel.nextCheckTime)
        }

        if (dashboardModel.studentCount !== null) {
            binding.tvDashboardStudent.text = dashboardModel.studentCount.toString()
        }

        binding.frameTitleContainer.setBackgroundColor(Color.parseColor(dashboardModel.backgroundColor))
    }
}