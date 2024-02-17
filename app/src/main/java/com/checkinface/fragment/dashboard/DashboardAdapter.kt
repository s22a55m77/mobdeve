package com.checkinface.fragment.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.R
import com.checkinface.databinding.DashboardItemLayoutBinding
import com.checkinface.util.UserRole
import com.checkinface.util.UserSharedPreference

class DashboardAdapter(private val data: ArrayList<DashboardModel>): Adapter<DashboardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DashboardItemLayoutBinding.inflate(inflater, parent, false)

        val userSharedPreference = UserSharedPreference(parent.context)
        if (userSharedPreference.getRole()?.equals(UserRole.STUDENT) == true) {
            binding.tvDashboardStudentTitle.visibility = View.GONE
            binding.tvDashboardStudent.visibility = View.GONE
            binding.llTeacherSideStudentCount.visibility = View.GONE
        } else if (userSharedPreference.getRole()?.equals(UserRole.TEACHER) == true) {
            binding.tvDashboardCheckTimeTitle.visibility = View.GONE
            binding.tvDashboardCheckTime.visibility = View.GONE
            binding.ivNextCheckTimeIcon.visibility = View.GONE
        }

        return DashboardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bindData(data[position])

        holder.itemView.setOnClickListener {
            val navController = holder.itemView.findNavController()

            val user = UserSharedPreference(holder.itemView.context)
            if (user.getRole()?.equals(UserRole.STUDENT) == true) {
                navController.navigate(R.id.action_dashboard_to_course)
                navController.currentDestination?.label = data[position].course
            } else if (user.getRole()?.equals(UserRole.TEACHER) == true) {
                navController.navigate(R.id.action_dashboard_to_teacher_course)
                navController.currentDestination?.label = data[position].course
            }
        }
    }
}