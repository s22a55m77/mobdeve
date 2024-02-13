package com.checkinface.fragment.dashboard

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.R
import com.checkinface.databinding.DashboardItemLayoutBinding
import com.checkinface.util.DateUtil
import com.checkinface.util.UserSharedPreference

class DashboardViewHolder(private val binding: DashboardItemLayoutBinding): ViewHolder(binding.root) {
    fun bindData(dashboardModel: DashboardModel) {
        binding.tvDashboardCourse.text = dashboardModel.course
        if (dashboardModel.nextCheckTime !== null) {
            binding.tvDashboardCheckTime.text = DateUtil.getFormattedDate(dashboardModel.nextCheckTime)
        }

        if (dashboardModel.studentCount !== null) {
            binding.tvDashboardStudent.text = dashboardModel.studentCount.toString()
        }

        binding.frameTitleContainer.setBackgroundColor(Color.parseColor(dashboardModel.backgroundColor))
    }
}