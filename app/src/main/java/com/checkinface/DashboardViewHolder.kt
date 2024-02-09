package com.checkinface

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class DashboardViewHolder(itemView: View): ViewHolder(itemView) {
    private val dashboardCourseTv: TextView = itemView.findViewById(R.id.dashboardCourseTv);
    private val dashboardPresentChip: TextView = itemView.findViewById(R.id.dashboardPresentChip);
    private val dashboardAbsentChip: TextView = itemView.findViewById(R.id.dashboardAbsentChip);

    fun bindData() {

    }
}