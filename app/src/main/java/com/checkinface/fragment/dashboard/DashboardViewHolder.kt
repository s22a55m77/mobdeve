package com.checkinface.fragment.dashboard

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.R
import com.checkinface.util.DateUtil

class DashboardViewHolder(itemView: View): ViewHolder(itemView) {
    private val tvDashboardCourse: TextView = itemView.findViewById(R.id.tv_dashboard_course)
    private val tvDashboardCheckTime: TextView = itemView.findViewById(R.id.tv_dashboard_check_time)
    private val frameTitleContainer: FrameLayout = itemView.findViewById(R.id.frame_title_container)

    fun bindData(dashboardModel: DashboardModel) {
        tvDashboardCourse.text = dashboardModel.course
        if (dashboardModel.nextCheckTime !== null) {
            tvDashboardCheckTime.text = DateUtil.getFormattedDate(dashboardModel.nextCheckTime)
        }

        else
            tvDashboardCheckTime.visibility = View.GONE

        frameTitleContainer.setBackgroundColor(Color.parseColor(dashboardModel.backgroundColor))
    }
}