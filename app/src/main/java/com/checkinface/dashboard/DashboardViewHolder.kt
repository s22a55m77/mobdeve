package com.checkinface.dashboard

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.checkinface.R
import com.checkinface.model.DashboardModel

class DashboardViewHolder(itemView: View): ViewHolder(itemView) {
    private val dashboardCourseTv: TextView = itemView.findViewById(R.id.dashboardCourseTv);
    private val dashboardPresentChip: TextView = itemView.findViewById(R.id.dashboardPresentChip);
    private val dashboardAbsentChip: TextView = itemView.findViewById(R.id.dashboardAbsentChip);

    fun bindData(dashboardModel: DashboardModel) {
        dashboardCourseTv.text = dashboardModel.course
        dashboardPresentChip.text = dashboardModel.present.toString()
        dashboardAbsentChip.text = dashboardModel.absent.toString()
    }
}