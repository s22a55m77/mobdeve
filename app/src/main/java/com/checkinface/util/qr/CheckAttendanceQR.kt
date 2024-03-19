package com.checkinface.util.qr

import kotlinx.serialization.Serializable

@Serializable
class CheckAttendanceQR(val eventId: String, val courseCode: String): CommonQR(ACTION_CHECK) {
}