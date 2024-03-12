package com.checkinface.util

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.checkinface.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Calendar
import kotlin.random.Random

class FirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle FCM messages here
        Log.d("RECEIVE MESSAGE", remoteMessage.data.toString())

        // there is a new attendance event
        val type = remoteMessage.data["type"].toString()
        if (type == "add") {
            val courseName = remoteMessage.data["courseName"].toString()
            val time = remoteMessage.data["eventDateMessage"].toString()
            val notificationId = remoteMessage.data["notificationId"]?.toInt()!!

            sendAddAttendanceNotification(courseName, time)

            // schedule the notification before attendance checking
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, remoteMessage.data["year"]?.toInt()!!)
            calendar.set(Calendar.MONTH, remoteMessage.data["month"]?.toInt()!!)
            calendar.set(Calendar.DAY_OF_MONTH, remoteMessage.data["day"]?.toInt()!!)
            calendar.set(Calendar.HOUR_OF_DAY, remoteMessage.data["hour"]?.toInt()!!)
            // minus 10 to set the notification 10 min before the attendance
            calendar.set(Calendar.MINUTE, remoteMessage.data["minute"]?.toInt()!! - 10)
            calendar.set(Calendar.SECOND, remoteMessage.data["second"]?.toInt()!!)
            scheduleNotification(calendar, courseName, notificationId)
        } else if (type == "modify") {
            val courseName = remoteMessage.data["courseName"].toString()
            val time = remoteMessage.data["eventDateMessage"].toString()
            val notificationId = remoteMessage.data["notificationId"]?.toInt()!!

            sendModifyAttendanceNotification(courseName, time)

            // schedule the notification before attendance checking
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, remoteMessage.data["year"]?.toInt()!!)
            calendar.set(Calendar.MONTH, remoteMessage.data["month"]?.toInt()!!)
            calendar.set(Calendar.DAY_OF_MONTH, remoteMessage.data["day"]?.toInt()!!)
            calendar.set(Calendar.HOUR_OF_DAY, remoteMessage.data["hour"]?.toInt()!!)
            // minus 10 to set the notification 10 min before the attendance
            calendar.set(Calendar.MINUTE, remoteMessage.data["minute"]?.toInt()!! - 10)
            calendar.set(Calendar.SECOND, remoteMessage.data["second"]?.toInt()!!)
            scheduleNotification(calendar, courseName, notificationId)
        } else if (type == "delete") {
            val notificationId = remoteMessage.data["notificationId"]?.toInt()!!
            val courseName = remoteMessage.data["courseName"].toString()
            deleteScheduledNotification(courseName, notificationId)
        }
    }

    override fun onNewToken(token: String) {
        Log.d("TOKEN", token)
    }

    private fun sendModifyAttendanceNotification(courseName: String, time: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(this, NotificationUtil.ATTENDANCE_EVENT_CHANNEL)
            .setSmallIcon(R.drawable.ic_attendance)
            .setContentTitle("Attendance Modified!")
            .setContentText("An Attendance Modified For $courseName")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("An attendance has been modified for $courseName to $time"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val randomInt = Random.nextInt()
        notificationManager.notify(randomInt, builder.build())
    }

    private fun sendAddAttendanceNotification(courseName: String, time: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(this, NotificationUtil.ATTENDANCE_EVENT_CHANNEL)
            .setSmallIcon(R.drawable.ic_attendance)
            .setContentTitle("New Attendance Created!")
            .setContentText("New Attendance For $courseName")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("A new attendance has been created for $courseName at $time"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val randomInt = Random.nextInt()
        notificationManager.notify(randomInt, builder.build())
    }

    private fun deleteScheduledNotification(courseName: String, notificationId: Int) {
        val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(applicationContext, AttendanceNotificationReceiver::class.java)
        intent.putExtra("notificationId", notificationId)
        intent.putExtra("courseName", courseName)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.cancel(pendingIntent)
    }

    private fun scheduleNotification(notificationTime: Calendar, courseName: String, notificationId: Int) {
        val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(applicationContext, AttendanceNotificationReceiver::class.java)
        intent.putExtra("notificationId", notificationId)
        intent.putExtra("courseName", courseName)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime.timeInMillis, pendingIntent)
    }
}