package com.checkinface.fragment.dashboard

import java.util.Date

data class DashboardModel(
    val course: String,
    val backgroundColor: String,
    val nextCheckTime: Date?,
    val studentCount: Number?
)
