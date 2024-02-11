package com.checkinface.fragment.dashboard

import java.text.SimpleDateFormat

data class DashboardModel(
    val course: String,
    val nextCheckTime: SimpleDateFormat
)
