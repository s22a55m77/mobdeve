package com.checkinface.fragment.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.R
import com.checkinface.databinding.DashboardItemLayoutBinding
import com.checkinface.util.VariableHolder
import com.checkinface.util.UserRole

class DashboardAdapter(private val data: ArrayList<DashboardModel>, private val role: UserRole): Adapter<DashboardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DashboardItemLayoutBinding.inflate(inflater, parent, false)

        if (role === UserRole.STUDENT) {
            binding.tvDashboardStudentTitle.visibility = View.GONE
            binding.tvDashboardStudent.visibility = View.GONE
            binding.llTeacherSideStudentCount.visibility = View.GONE
        } else if (role === UserRole.TEACHER) {
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
            VariableHolder.getInstance().courseCode = data[position].courseCode
//            val sp = holder.itemView.rootView.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
//            with(sp.edit()) {
//                putString("COURSE_CODE", data[position].courseCode)
//                apply()
//            }

            if (role === UserRole.STUDENT) {
                navController.navigate(R.id.action_dashboard_to_course)
                navController.currentDestination?.label = data[position].course
            } else if (role === UserRole.TEACHER) {
                navController.navigate(R.id.action_dashboard_to_teacher_course)
                navController.currentDestination?.label = data[position].course
            }
        }
    }
}