package com.checkinface.util

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.checkinface.R

class AttendanceNotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = intent.getIntExtra("notificationId", 0)
        val courseName = intent.getStringExtra("courseName")

        val builder = NotificationCompat.Builder(context, NotificationUtil.ATTENDANCE_CHECKING_CHANNEL)
            .setSmallIcon(R.drawable.ic_attendance)
            .setContentTitle("Attendance Checking Coming!")
            .setContentText("Attendance For $courseName")
            .setStyle(
                NotificationCompat.BigTextStyle()
                .bigText("An attendance check for $courseName is scheduled in 10 minutes"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(notificationId, builder.build())
    }
}