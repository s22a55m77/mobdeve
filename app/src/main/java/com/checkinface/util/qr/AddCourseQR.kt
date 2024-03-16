package com.checkinface.util.qr

import kotlinx.serialization.Serializable

@Serializable
data class AddCourseQR(val courseCode: String): CommonQR(ACTION_ADD) {
}