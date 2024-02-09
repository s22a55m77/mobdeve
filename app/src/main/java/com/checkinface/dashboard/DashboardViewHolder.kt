package com.checkinface.dashboard

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.R
import com.checkinface.model.DashboardModel
import java.util.Date

class DashboardViewHolder(itemView: View): ViewHolder(itemView) {
    private val dashboardCourseTv: TextView = itemView.findViewById(R.id.dashboardCourseTv);
    private val dashboardCheckTimeTv: TextView = itemView.findViewById(R.id.dashboardCheckTimeTv);

    fun bindData(dashboardModel: DashboardModel) {
        dashboardCourseTv.text = dashboardModel.course
        dashboardCheckTimeTv.text = dashboardModel.nextCheckTime.format(Date(System.currentTimeMillis()))
    }
}