package com.checkinface.fragment.dashboard

import java.text.SimpleDateFormat

data class DashboardModel(
    val course: String,
    val backgroundColor: String,
    val nextCheckTime: SimpleDateFormat?,
    val studentCount: Number?
)
