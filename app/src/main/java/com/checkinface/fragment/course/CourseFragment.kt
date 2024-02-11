package com.checkinface.fragment.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.checkinface.R
import com.checkinface.fragment.dashboard.DashboardAdapter
import com.checkinface.fragment.dashboard.DashboardDataGenerator

class CourseFragment : Fragment() {

    private val courseModelList = CourseDataGenerator.loadData()
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.recyclerView = view.findViewById(R.id.rv_course)

        val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
        this.recyclerView.layoutManager = linearLayoutManager

        this.recyclerView.adapter = CourseAdapter(this.courseModelList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false)
    }
}