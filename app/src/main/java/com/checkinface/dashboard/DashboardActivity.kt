package com.checkinface.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.checkinface.R
import com.checkinface.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private val dashboardModelList = DashboardDataGenerator.loadData()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        this.recyclerView = viewBinding.dashboardRv

        val gridLayoutManager = GridLayoutManager(this, 2)
        this.recyclerView.layoutManager = gridLayoutManager

        this.recyclerView.adapter = DashboardAdapter(this.dashboardModelList)
    }
}