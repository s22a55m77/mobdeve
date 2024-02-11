package com.checkinface.fragment.course

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.R

class CourseAdapter(private val data: ArrayList<CourseModel>): Adapter<CourseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.couse_item_layout, parent, false)

        return CourseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bindData(data[position])
    }

}