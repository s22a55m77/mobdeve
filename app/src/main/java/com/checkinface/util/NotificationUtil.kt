package com.checkinface.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi

class NotificationUtil {
    companion object {
        const val ATTENDANCE_EVENT_CHANNEL  = "1"
        const val ATTENDANCE_CHECKING_CHANNEL = "2"
    }

    fun createChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createAttendanceEventChannel(notificationManager)
            createAttendanceCheckingChannel(notificationManager)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAttendanceEventChannel (notificationManager: NotificationManager) {
        val channelId = ATTENDANCE_EVENT_CHANNEL
        val channelName = "Attendance Event Notification"
        val channelDescription = "Attendance Event Notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAttendanceCheckingChannel (notificationManager: NotificationManager) {
        val channelId = ATTENDANCE_CHECKING_CHANNEL
        val channelName = "Attendance Checking Notification"
        val channelDescription = "Attendance Checking Notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }
        notificationManager.createNotificationChannel(channel)
    }
}