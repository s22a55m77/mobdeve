package com.checkinface.model

import java.text.SimpleDateFormat

data class DashboardModel(
    val course: String,
    val nextCheckTime: SimpleDateFormat
)
