package com.checkinface.fragment.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.R
import com.checkinface.fragment.course.CourseFragment

class DashboardAdapter(private val data: ArrayList<DashboardModel>): Adapter<DashboardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.dashboard_item_layout, parent, false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bindData(data[position])

        holder.itemView.setOnClickListener {
            holder.itemView.findNavController().navigate(R.id.action_dashboard_to_course)
        }
    }
}